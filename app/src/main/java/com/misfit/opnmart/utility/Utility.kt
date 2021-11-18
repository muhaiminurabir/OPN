package com.misfit.opnmart.utility

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.misfit.opnmart.R
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    var context: Context? = null
    var mProgressDialog: ProgressDialog? = null

    fun Utility(context: Context?) {
        this.context = context
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        freeMemory()
    }

    //================ Show Progress Dialog ===============
    fun showProgress(isCancelable: Boolean, message: String?) {
        hideProgress()
        mProgressDialog = ProgressDialog(context, R.style.AppCompatAlertDialogStyle)
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setCancelable(isCancelable)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.show()
    }

    //================ Hide Progress Dialog ===============
    fun hideProgress() {
        try {
            if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            Log.d("Error Line Number", Log.getStackTraceString(e))
        }
    }

    fun isNetworkAvailable(): Boolean {
        val manager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return info != null && info.isConnected
    }


    /*
    ================ Show Toast Message ===============
    */
    fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /*
    =============== Set Window FullScreen ===============
    */
    fun setFullScreen() {
        val activity = context as Activity?
        activity!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    /*
        ================ Get Screen Width ===============
        */
    fun getScreenRes(): HashMap<String, Int>? {
        val map = HashMap<String, Int>()
        val metrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        map[Keyword.SCREEN_WIDTH] = width
        map[Keyword.SCREEN_HEIGHT] = height
        map[Keyword.SCREEN_DENSITY] = metrics.density.toInt()
        return map
    }


    /*
    ================ Log function ===============
     */
    fun logger(message: String?) {
        Log.d(context!!.getString(R.string.app_name), message!!)
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        val date = sdf.format(Date())
        //writeToFile(date+" -> "+message);
    }

    /*
    ================ Clear Text for EditText, Button, TextView ===============
    */
    fun clearText(view: Array<View?>) {
        for (v in view) {
            if (v is EditText) {
                v.setText("")
            } else if (v is Button) {
                v.text = ""
            } else if (v is TextView) {
                v.text = ""
            }
        }
    }

    /*
    ================ Hide Keyboard from Screen ===============
    */
    fun hideKeyboard(view: View) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    /*
    ================ Show Keyboard to Screen ===============
    */
    fun showKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }
}
