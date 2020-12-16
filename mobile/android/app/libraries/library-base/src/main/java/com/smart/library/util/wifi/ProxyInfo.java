/*
 * Copyright (C) 2010 The Android Open Source Project
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


import android.net.Proxy;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URLConnection;
import java.util.Locale;

/**
 * Describes a proxy configuration.
 * <p>
 * Proxy configurations are already integrated within the {@code java.net} and
 * Apache HTTP stack. So {@link URLConnection} and Apache's {@code HttpClient} will use
 * them automatically.
 * <p>
 * Other HTTP stacks will need to obtain the proxy info from
 * {@link Proxy#PROXY_CHANGE_ACTION} broadcast as the extra {@link Proxy#EXTRA_PROXY_INFO}.
 */
public class ProxyInfo implements Parcelable {

    private String mHost;
    private int mPort;
    private String mExclusionList;
    private String[] mParsedExclusionList;

    private Uri mPacFileUrl;
    /**
     * @hide
     */
    public static final String LOCAL_EXCL_LIST = "";
    /**
     * @hide
     */
    public static final int LOCAL_PORT = -1;
    /**
     * @hide
     */
    public static final String LOCAL_HOST = "localhost";

    /**
     * Only used in PacManager after Local Proxy is bound.
     *
     * @hide
     */
    public ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        mHost = LOCAL_HOST;
        mPort = localProxyPort;
        setExclusionList(LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        mPacFileUrl = pacFileUrl;
    }

    private ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        mHost = host;
        mPort = port;
        mExclusionList = exclList;
        mParsedExclusionList = parsedExclList;
        mPacFileUrl = Uri.EMPTY;
    }

    /**
     * Returns the URL of the current PAC script or null if there is
     * no PAC script.
     */
    public Uri getPacFileUrl() {
        return mPacFileUrl;
    }

    /**
     * When configured to use a Direct Proxy this returns the host
     * of the proxy.
     */
    public String getHost() {
        return mHost;
    }

    /**
     * comma separated
     *
     * @hide
     */
    public String getExclusionListAsString() {
        return mExclusionList;
    }

    // comma separated
    private void setExclusionList(String exclusionList) {
        mExclusionList = exclusionList;
        if (mExclusionList == null) {
            mParsedExclusionList = new String[0];
        } else {
            mParsedExclusionList = exclusionList.toLowerCase(Locale.ROOT).split(",");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(mPacFileUrl);
        }
        if (mHost != null) {
            sb.append("[");
            sb.append(mHost);
            sb.append("] ");
            sb.append(Integer.toString(mPort));
            if (mExclusionList != null) {
                sb.append(" xl=").append(mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProxyInfo)) return false;
        ProxyInfo p = (ProxyInfo) o;
        // If PAC URL is present in either then they must be equal.
        // Other parameters will only be for fall back.
        if (!Uri.EMPTY.equals(mPacFileUrl)) {
            return mPacFileUrl.equals(p.getPacFileUrl()) && mPort == p.mPort;
        }
        if (!Uri.EMPTY.equals(p.mPacFileUrl)) {
            return false;
        }
        if (mExclusionList != null && !mExclusionList.equals(p.getExclusionListAsString())) {
            return false;
        }
        if (mHost != null && p.getHost() != null && mHost.equals(p.getHost()) == false) {
            return false;
        }
        if (mHost != null && p.mHost == null) return false;
        if (mHost == null && p.mHost != null) return false;
        if (mPort != p.mPort) return false;
        return true;
    }

    /**
     * Implement the Parcelable interface
     *
     * @hide
     */
    public int describeContents() {
        return 0;
    }

    @Override
    /*
     * generate hashcode based on significant fields
     */
    public int hashCode() {
        return ((null == mHost) ? 0 : mHost.hashCode())
                + ((null == mExclusionList) ? 0 : mExclusionList.hashCode())
                + mPort;
    }

    /**
     * Implement the Parcelable interface.
     *
     * @hide
     */
    public void writeToParcel(Parcel dest, int flags) {
        if (!Uri.EMPTY.equals(mPacFileUrl)) {
            dest.writeByte((byte) 1);
            mPacFileUrl.writeToParcel(dest, 0);
            dest.writeInt(mPort);
            return;
        } else {
            dest.writeByte((byte) 0);
        }
        if (mHost != null) {
            dest.writeByte((byte) 1);
            dest.writeString(mHost);
            dest.writeInt(mPort);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeString(mExclusionList);
        dest.writeStringArray(mParsedExclusionList);
    }

    public static final Creator<ProxyInfo> CREATOR =
            new Creator<ProxyInfo>() {
                public ProxyInfo createFromParcel(Parcel in) {
                    String host = null;
                    int port = 0;
                    if (in.readByte() != 0) {
                        Uri url = Uri.CREATOR.createFromParcel(in);
                        int localPort = in.readInt();
                        return new ProxyInfo(url, localPort);
                    }
                    if (in.readByte() != 0) {
                        host = in.readString();
                        port = in.readInt();
                    }
                    String exclList = in.readString();
                    String[] parsedExclList = new String[100];
                    in.readStringArray(parsedExclList);
                    ProxyInfo proxyProperties =
                            new ProxyInfo(host, port, exclList, parsedExclList);
                    return proxyProperties;
                }

                public ProxyInfo[] newArray(int size) {
                    return new ProxyInfo[size];
                }
            };
}
