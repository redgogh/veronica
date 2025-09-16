"""

    Copyright (C) 2019-2024 RedGogh All rights reserved.

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
RED = '\033[31m'
GREEN = '\033[32m'
YELLOW = '\033[33m'
BLUE = '\033[34m'
PURPLE = '\033[35m'
WHITE = '\033[37m'
END = '\033[0m'

def write(text, color=GREEN):
    """
    向控制台输出带颜色的文本符号

    该文件定义了多种可选颜色。同时用户也可以不用选择颜色输出。让控制台的
    输出更清晰。

    :param text: 输出文本
    :param color: 输出颜色
    """
    if color is not None:
        print(f'{color}{text}{END}')
    else:
        print(f'{text}')
