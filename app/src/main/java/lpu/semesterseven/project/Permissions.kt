package lpu.semesterseven.project

import java.util.concurrent.CountDownLatch

var permissionCounter: Int = 0

data class PermissionRequest(var permissions: Array<String>, var requestCode: Int){
    val latch: CountDownLatch = CountDownLatch(1)
    var result: Boolean = false
}