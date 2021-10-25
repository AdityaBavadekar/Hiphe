/*******************************************************************************
 * Copyright (c) 2021. Aditya Bavadekar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 ******************************************************************************/

/*******************************************************************************
 * Copyright (c) 2021. Aditya Bavadekar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 ******************************************************************************/

/*******************************************************************************
 * Copyright (c) 2021. Aditya Bavadekar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 ******************************************************************************/

package com.adityaamolbavadekar.hiphe

import android.net.Uri
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase


fun createDynamicLink(name: String): Uri {


    try {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.hiphe.com/user/${name.removeSuffix("@gmail.com")}")
            domainUriPrefix = "https://hiphe.page.link/"
            androidParameters("com.adityaamolbavadekar.hiphe") {
                this.build()
            }
            socialMetaTagParameters {
                title = "${name.substring(10)}... on Hiphe"
                description = "View profile of ${name.substring(10)}... on Hiphe"
                imageUrl = Uri.parse("https://i.ibb.co/d7L6Jc8/default-account-image.png")
            }

        }
        return dynamicLink.uri
    } catch (e: Exception) {
        val link = Uri.parse("https://www.hiphe.com/user/${name.removeSuffix("@gmail.com")}")
        return link

    }

}

fun createDynamicLink2(name: String): Uri {

    val deepLink = Uri.parse("https://hiphe.adityaamolbavadekar.com/users/${name.trim()}")
    val uriPrefix = "https://hiphe.page.link"
    val link = Firebase.dynamicLinks.dynamicLink {
        domainUriPrefix = uriPrefix
        androidParameters {
            minimumVersion = 22
        }
        link = deepLink
    }
    return link.uri


}

