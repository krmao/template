/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.net.LinkAddress;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that describes static IP configuration.
 * <p>
 * This class is different from LinkProperties because it represents
 * configuration intent. The general contract is that if we can represent
 * a configuration here, then we should be able to configure it on a network.
 * The intent is that it closely match the UI we have for configuring networks.
 * <p>
 * In contrast, LinkProperties represents current state. It is much more
 * expressive. For example, it supports multiple IP addresses, multiple routes,
 * stacked interfaces, and so on. Because LinkProperties is so expressive,
 * using it to represent configuration intent as well as current state causes
 * problems. For example, we could unknowingly save a configuration that we are
 * not in fact capable of applying, or we could save a configuration that the
 * UI cannot display, which has the potential for malicious code to hide
 * hostile or unexpected configuration from the user: see, for example,
 * http://b/12663469 and http://b/16893413 .
 */
public class StaticIpConfiguration implements Parcelable {
    public LinkAddress ipAddress;
    public InetAddress gateway;
    public final ArrayList<InetAddress> dnsServers;
    public String domains;

    public StaticIpConfiguration() {
        dnsServers = new ArrayList<InetAddress>();
    }

    public void clear() {
        ipAddress = null;
        gateway = null;
        dnsServers.clear();
        domains = null;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append("IP address ");
        if (ipAddress != null) str.append(ipAddress).append(" ");

        str.append("Gateway ");
        if (gateway != null) str.append(gateway.getHostAddress()).append(" ");

        str.append(" DNS servers: [");
        for (InetAddress dnsServer : dnsServers) {
            str.append(" ").append(dnsServer.getHostAddress());
        }

        str.append(" ] Domains ");
        if (domains != null) str.append(domains);
        return str.toString();
    }

    public int hashCode() {
        int result = 13;
        result = 47 * result + (ipAddress == null ? 0 : ipAddress.hashCode());
        result = 47 * result + (gateway == null ? 0 : gateway.hashCode());
        result = 47 * result + (domains == null ? 0 : domains.hashCode());
        result = 47 * result + dnsServers.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof StaticIpConfiguration)) return false;

        StaticIpConfiguration other = (StaticIpConfiguration) obj;

        return other != null &&
                Objects.equals(ipAddress, other.ipAddress) &&
                Objects.equals(gateway, other.gateway) &&
                dnsServers.equals(other.dnsServers) &&
                Objects.equals(domains, other.domains);
    }

    /**
     * Implement the Parcelable interface
     */
    public static Creator<StaticIpConfiguration> CREATOR =
            new Creator<StaticIpConfiguration>() {
                public StaticIpConfiguration createFromParcel(Parcel in) {
                    StaticIpConfiguration s = new StaticIpConfiguration();
                    readFromParcel(s, in);
                    return s;
                }

                public StaticIpConfiguration[] newArray(int size) {
                    return new StaticIpConfiguration[size];
                }
            };

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ipAddress, flags);
        NetworkUtils.parcelInetAddress(dest, gateway, flags);
        dest.writeInt(dnsServers.size());
        for (InetAddress dnsServer : dnsServers) {
            NetworkUtils.parcelInetAddress(dest, dnsServer, flags);
        }
        dest.writeString(domains);
    }

    protected static void readFromParcel(StaticIpConfiguration s, Parcel in) {
        s.ipAddress = in.readParcelable(null);
        s.gateway = NetworkUtils.unparcelInetAddress(in);
        s.dnsServers.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            s.dnsServers.add(NetworkUtils.unparcelInetAddress(in));
        }
        s.domains = in.readString();
    }
}
