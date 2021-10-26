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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.adityaamolbavadekar.hiphe.HipheErrorLog
import com.adityaamolbavadekar.hiphe.HipheInfoLog
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.models.JsonPlaceholderApi
import com.adityaamolbavadekar.hiphe.models.Post
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val jsonPlaceholderApi: JsonPlaceholderApi = retrofit.create(JsonPlaceholderApi::class.java)

        val call = jsonPlaceholderApi.getPostsCall()


        val callback = object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

//                val postList = mutableListOf<Post>()
                if (response.isSuccessful) {
                    val postList: List<Post>? = response.body()
                    HipheInfoLog(TAG, "@GET request was Successful")
                    HipheInfoLog(TAG, "@GET request's headers ${response.headers()}")
                    HipheInfoLog(TAG, "@GET request's raw ${response.raw()}")
                    var text: String = ""
                    postList?.forEach { post: Post ->
                        text += "\n\nPost : ${post.id}\n" +
                                "       Title : ${post.title.toUpperCase(Locale.ROOT)}\n"
                        "       Body : ${post.text}"
                        "               By UserId : ${
                            post.userId.toString().toUpperCase(Locale.ROOT)
                        }\n\n"
                    }
                    homeViewModel.setText(text)

                } else {
                    HipheErrorLog(TAG, "onResponse: Unseccessful: ", response.code().toString())
                    homeViewModel.setText(getString(R.string.oops_something_went_wrong))
                    return
                }

            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                HipheErrorLog(TAG, "onFailure: LOCALIZED MESSAGE: ", " ${t.localizedMessage}")
                HipheErrorLog(TAG, "onFailure: STACKTRACE: ", t.stackTraceToString())
                HipheErrorLog(TAG, "onFailure: DATA: ", t.cause.toString())
                homeViewModel.setText(getString(R.string.oops_something_went_wrong))
            }

        }

        call.enqueue(callback)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.button_home).setOnClickListener {
            val action = HomeFragmentDirections
                    .actionHomeFragmentToHomeSecondFragment("From HomeFragment")
            NavHostFragment.findNavController(this@HomeFragment)
                    .navigate(action)
        }
    }

    companion object {
        const val TAG = "HomeFragment"
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

}