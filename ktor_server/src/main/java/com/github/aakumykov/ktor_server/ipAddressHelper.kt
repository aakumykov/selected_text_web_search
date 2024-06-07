package com.github.aakumykov.ktor_server

import java.net.NetworkInterface

fun getIpAddressInLocalNetwork(): String? {

    val networkInterfaces = NetworkInterface.getNetworkInterfaces().iterator().asSequence()

    val localAddresses = networkInterfaces.flatMap {
        it.inetAddresses.asSequence()
            .filter { inetAddress ->
                inetAddress.isSiteLocalAddress
                        && !inetAddress.hostAddress.contains(":")
                        && inetAddress.hostAddress != "127.0.0.1"
            }
            .map { inetAddress -> inetAddress.hostAddress }
    }

    return localAddresses.firstOrNull()
}

val localIpAddress: String = getIpAddressInLocalNetwork() ?: DEFAULT_SERVER_IP

val serverPort: Int = DEFAULT_SERVER_PORT