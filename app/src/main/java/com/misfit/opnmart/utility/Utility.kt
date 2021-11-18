package com.misfit.opnmart.utility

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.misfit.opnmart.R
import java.text.SimpleDateFormat
import java.util.*

class Utility(private val context: Context) {
    init {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        freeMemory()
    }

    /*
    ================ Show Toast Message ===============
    */
    fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    /*
    ================ Log function ===============
     */
    fun logger(message: String?) {
        Log.d(context.getString(R.string.app_name), message!!)
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        val date = sdf.format(Date())
        //writeToFile(date+" -> "+message);
    }

    /*
    ================ Hide Keyboard from Screen ===============
    */
    fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }
}
