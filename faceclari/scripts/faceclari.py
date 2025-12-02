"""

    Copyright (C) 2019-2024 Viakko All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

"""
import PIL
import cv2
import face_recognition
import os
from pathlib import Path
import configparser
import time
import logging
from tqdm import tqdm
import yaml

logging.basicConfig(level="INFO", format='%(asctime)s - %(levelname)s - %(message)s')

# Initialize properties config file.
config = configparser.ConfigParser()

with open("faceclari.ini", "r", encoding="UTF-8") as faceclari_ini_file:
    config.read_file(faceclari_ini_file)

# Value of config.
# [recognition]
model = config.get("recognition", "recognition.model")
tolerance = config.getfloat("recognition", "recognition.tolerance")
drawrect = config.getboolean("recognition", "write.drawrect")
# [dir]
sample_faces_dir = config.get("dir", "sample")
matched_faces_dir = config.get("dir", "matched")
scan_dir = config.get("dir", "scandir")

kfaces = []


class FaceFeature:
    """
    Extract the face feature structure, Use this class to reduce the number of
    image encodings, making the application run faster.
    """

    def __init__(self, pathname, image, lenc, lloc):
        self.pathname = pathname
        self.image = image
        self.lenc = lenc
        self.lloc = lloc

    def include_face(self):
        """
        Check if the image has a face or not.
        """
        return len(self.lenc) > 0


EMPTY_FACE_FEATURE = FaceFeature(None, None, [], [])


def typecheck(pathname):
    """
    Check if the file is image or not.
    """
    return Path(pathname).suffix in [".jpg", ".png", "jpeg"]


def face_feature_extract(pathname):
    """
    Extract face encodings and locations from image.
    """
    if not typecheck(pathname):
        return EMPTY_FACE_FEATURE

    try:
        lenc = []

        image = face_recognition.load_image_file(pathname)
        lloc = face_recognition.face_locations(image, model=model)

        if len(lloc) > 0:
            lenc = face_recognition.face_encodings(image, lloc, model=model)

        return FaceFeature(pathname, image, lenc, lloc)
    except PIL.UnidentifiedImageError as e:
        logging.error(f"{e}")
        return EMPTY_FACE_FEATURE


class KFaceSample:
    """
    Loaded all the known face encodings and locations in memory. Use this
    class to compare other faces.
    """

    def __init__(self, dirname, lenc, lloc):
        self.dirname = Path(dirname)
        self.lenc = lenc
        self.lloc = lloc
        self.name = self.dirname.stem

    def compare(self, face):
        """
        Compare the known face data with to unknown face data. if similarity
        is high return true otherwise return false.
        """

        # if the image does not have a face
        if not face.include_face():
            return False

        matchs = None
        find = False

        for cmpenc, cmploc in zip(self.lenc, self.lloc):
            matchs = face_recognition.compare_faces(face.lenc, cmpenc, tolerance=tolerance)
            find = True in matchs
            if find:
                break

        if find:
            origin = cv2.cvtColor(face.image, cv2.COLOR_RGB2BGR)

            # draw rectangle on matched face
            if drawrect:
                matched_index = matchs.index(True)
                top, right, bottom, left = face.lloc[matched_index]
                cv2.rectangle(origin, (left, top), (right, bottom), (255, 0, 0), 2)

            final_dir = f"{matched_faces_dir}/{self.name}"
            if not os.path.exists(final_dir):
                os.makedirs(final_dir)

            cv2.imwrite(f"{final_dir}/{os.path.basename(face.pathname)}", origin)

        return find


def load_known_faces():
    """
    loading all the known faces in known_faces directory. Each image in the
    known_faces directory must include one face.
    """
    ret_kfaces_val = []

    for item in os.listdir(sample_faces_dir):
        face_dir = os.path.join(sample_faces_dir, item)
        if Path(face_dir).is_dir():
            # face encodings and locations.
            sample_face_lenc = []
            sample_face_lloc = []

            for image in os.listdir(face_dir):
                abs_file_path = Path(f"{face_dir}/{image}").resolve()
                load_feature = face_feature_extract(abs_file_path)

                if not load_feature.include_face():
                    continue

                sample_face_lenc.append(load_feature.lenc[0])
                sample_face_lloc.append(load_feature.lloc[0])

            ret_kfaces_val.append(KFaceSample(face_dir, sample_face_lenc, sample_face_lloc))

    return ret_kfaces_val


if __name__ == "__main__":
    start_time = time.time()

    # create dir if not exists.
    os.makedirs(sample_faces_dir, exist_ok=True)
    os.makedirs(matched_faces_dir, exist_ok=True)
    os.makedirs(scan_dir, exist_ok=True)

    # loading faces
    kfaces = load_known_faces()
    files = []

    for dirpath, dirnames, filenames in os.walk(scan_dir):
        for filename in filenames:
            abs_path = os.path.join(dirpath, filename)
            if typecheck(abs_path):
                files.append(abs_path)

    for file in tqdm(iterable=files):
        feature = face_feature_extract(file)
        include_face = feature.include_face()

        if include_face:
            for kface in kfaces:
                kface.compare(feature)

    end_time = time.time()

    elapsed_time = end_time - start_time
    minutes = int(elapsed_time // 60)
    seconds = int(elapsed_time % 60)

    logging.info(f"Total time taken {minutes} min {seconds} sec.")
