/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smart.library.util.wifi;

public class Credentials {
    /**
     * Key prefix for CA certificates.
     */
    public static final String CA_CERTIFICATE = "CACERT_";

    /**
     * Key prefix for user private and secret keys.
     */
    public static final String USER_PRIVATE_KEY = "USRPKEY_";

    /**
     * Key prefix for user secret keys.
     *
     * @deprecated use {@code USER_PRIVATE_KEY} for this category instead.
     */
    public static final String USER_SECRET_KEY = "USRSKEY_";


    private static Credentials singleton;

    public static Credentials getInstance() {
        if (singleton == null) {
            singleton = new Credentials();
        }
        return singleton;
    }
}
