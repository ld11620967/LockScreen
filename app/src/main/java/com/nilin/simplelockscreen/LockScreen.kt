package com.nilin.simplelockscreen

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle


class LockScreen : Activity() {
    private var policyManager: DevicePolicyManager? = null
    private var componentName1: ComponentName? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取设备管理服务
        policyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName1 = ComponentName(this, AdminReceiver::class.java)
        mylock()
        //结束进程
        finish()
    }

    private fun mylock() {

        val active = policyManager!!.isAdminActive(componentName1!!)
        if (!active) {//若无权限
            activeManage()//去获得权限
        }
        if (active) {
            policyManager!!.lockNow()//直接锁屏
        }
    }

    private fun activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName1)
        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 需获取以下权限才可进行一键锁屏 ------")
        startActivityForResult(intent, 0)
    }
}


