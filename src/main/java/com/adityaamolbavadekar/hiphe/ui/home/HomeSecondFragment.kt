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

package com.adityaamolbavadekar.hiphe.ui.home

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.adityaamolbavadekar.hiphe.R

class HomeSecondFragment : Fragment() {

    private val args: HomeSecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home_second, container, false)
        try {
            val model = Build.MODEL
            val manufacturer = Build.MANUFACTURER
            val id = Build.ID
            val device = Build.DEVICE
            val brand = Build.BRAND
            val androidOs = Build.VERSION.SDK_INT
            val product = Build.PRODUCT
            val packageName = PackageInfo().packageName
            val versionName = PackageInfo().versionName
            val installLoacation = PackageInfo().installLocation
            val permissionsGranted = PackageInfo().permissions
            val versionCode = PackageInfo().versionCode
            val androidVersion = Build.VERSION.RELEASE
            val time = Build.TIME
            val user = Build.USER
            val freeSpace = Environment.getDataDirectory().freeSpace
            val totalSpace = Environment.getDataDirectory().totalSpace
            val data =
                "           DETAILS\nMODEL : $model\nMANUFACTURER : $manufacturer\nUSER : $user\nID : $id\nDEVICE : $device\nBRAND : $brand\nPRODUCT : $product \nAndroid SDK Version : $androidOs\nAndroid Version : $androidOs\nAndroid Version : $androidOs\nAndroid Version : $androidOs\nAndroid Release Version : $androidVersion\nFree Space: $freeSpace\nTotal Space : $totalSpace\n\n        ***APP***\nPackage Name : $packageName\nVersion Name : $versionName\nVersion Code : $versionCode\nBuild Time : $time\nBuild Time : $time\nInstall Location : $installLoacation\nPermissions: $permissionsGranted\n\n"

            root.findViewById<TextView>(R.id.textview_home_second2).text = data
        } catch (e: Exception) {
            root.findViewById<TextView>(R.id.textview_home_second2).text = "ERROR : $e"
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textview_home_second).setOnClickListener {
            throw Throwable("Custom crash")
        }
        view.findViewById<TextView>(R.id.textview_home_second).text =
            getString(R.string.hello_home_second, args.myArg)


        view.findViewById<Button>(R.id.button_home_second).setOnClickListener {
            findNavController().navigate(R.id.action_HomeSecondFragment_to_HomeFragment)
        }
    }
}