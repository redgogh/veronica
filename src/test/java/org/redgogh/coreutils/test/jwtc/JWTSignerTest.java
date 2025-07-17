package org.redgogh.coreutils.test.jwtc;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 RedGogh All rights reserved.                          *|
|*                                                                                  *|
|*    Licensed under the Apache License, Version 2.0 (the "License");               *|
|*    you may not use this file except in compliance with the License.              *|
|*    You may obtain a copy of the License at                                       *|
|*                                                                                  *|
|*        http://www.apache.org/licenses/LICENSE-2.0                                *|
|*                                                                                  *|
|*    Unless required by applicable law or agreed to in writing, software           *|
|*    distributed under the License is distributed on an "AS IS" BASIS,             *|
|*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.      *|
|*    See the License for the specific language governing permissions and           *|
|*    limitations under the License.                                                *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

import org.junit.Test;
import org.redgogh.coreutils.jwtc.JWTClaims;
import org.redgogh.coreutils.jwtc.JWTSigner;
import org.redgogh.coreutils.security.Crypt;
import org.redgogh.coreutils.security.key.RSAPrivateKey;
import org.redgogh.coreutils.security.key.RSAPublicKey;
import org.redgogh.coreutils.tuple.Pair;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import static org.redgogh.coreutils.generator.Generator.b32md5;

@SuppressWarnings("ALL")
public class JWTSignerTest {

    @Test
    public void hs256() throws InterruptedException {
        JWTSigner signer = new JWTSigner(b32md5());

        JWTClaims claims = new JWTClaims();
        claims.setExpirationTime(5, TimeUnit.SECONDS);
        String token = signer.signWith(claims);

        System.out.println(token);
        System.out.println("token is expired: " + signer.verify(token));
        Thread.sleep(5000);
        System.out.println("token is expired in 5 minutes: " + signer.verify(token));
    }

    @Test
    public void rs256() throws InterruptedException {
        Pair<RSAPublicKey, RSAPrivateKey> pair = Crypt.RSA.generateKeyPair();
        PublicKey publicKey = pair.first().toPublicKey();
        PrivateKey privateKey = pair.second().toPrivateKey();

        JWTSigner signer = new JWTSigner(publicKey, privateKey);

        JWTClaims claims = new JWTClaims();
        claims.setExpirationTime(5, TimeUnit.SECONDS);
        String token = signer.signWith(claims);

        System.out.println(token);
        System.out.println("token is expired: " + signer.verify(token));
        Thread.sleep(5000);
        System.out.println("token is expired in 5 minutes: " + signer.verify(token));
    }

}
