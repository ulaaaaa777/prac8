package com.example.kotlinlab11.task1

/**
 * 1-topshiriq. Kotlin modeli: rad etish holatlari ("Denied twice" modeli)
 */
class PermissionModel {
    var isGranted: Boolean = false
        private set
    var shouldShowRationale: Boolean = false
        private set
    var dialogShows: Boolean = false
        private set

    private var denyCount: Int = 0

    fun request(userAllows: Boolean) {
        if (isGranted) {
            dialogShows = false
            shouldShowRationale = false
            println("Ruxsat allaqachon berilgan.")
            return
        }

        if (denyCount >= 2) {
            // Ikki marta rad etilgandan keyin dialog ko'rinmaydi, darhol rad etiladi
            dialogShows = false
            shouldShowRationale = false
            println("Dialog yo'q, darhol rad (Sozlamalarga yo'naltirish kerak)")
            return
        }

        // Dialog ko'rsatiladi
        dialogShows = true

        if (userAllows) {
            isGranted = true
            shouldShowRationale = false
            denyCount = 0
            println("Foydalanuvchi ruxsat berdi.")
        } else {
            denyCount++
            isGranted = false
            if (denyCount == 1) {
                // Bir marta rad etilganda rationale = true
                shouldShowRationale = true
                println("Foydalanuvchi 1-marta rad etdi (Rationale = true).")
            } else if (denyCount == 2) {
                // Ikki marta rad etilganda rationale = false
                shouldShowRationale = false
                println("Foydalanuvchi 2-marta rad etdi (Permanently denied / Don't ask again).")
            }
        }
    }

    // Sozlamalardan ruxsat berilganda chaqiriladi
    fun grantFromSettings() {
        isGranted = true
        shouldShowRationale = false
        dialogShows = false
        denyCount = 0
        println("Sozlamalar orqali ruxsat berildi.")
    }

    fun printState(stepName: String) {
        println("[$stepName] -> granted=$isGranted, rationale=$shouldShowRationale, dialogShows=$dialogShows")
    }
}

fun main() {
    val permissionModel = PermissionModel()

    println("=== 1-TOP SHIRIQ: PERMISSION MODEL ('DENIED TWICE' SIMULYATSIYASI) ===")
    permissionModel.printState("Boshlang'ich holat")

    println("\n--- 1-so'rov: Foydalanuvchi rad etdi ---")
    permissionModel.request(userAllows = false)
    permissionModel.printState("1-so'rovdan keyin")

    println("\n--- 2-so'rov: Foydalanuvchi yana rad etdi ---")
    permissionModel.request(userAllows = false)
    permissionModel.printState("2-so'rovdan keyin")

    println("\n--- 3-so'rov: Yana launch() chaqirildi ---")
    permissionModel.request(userAllows = false)
    permissionModel.printState("3-so'rovdan keyin")

    println("\n--- 4-so'rov: Sozlamalar orqali ruxsat berildi ---")
    permissionModel.grantFromSettings()
    permissionModel.printState("Sozlamalardan keyin")
}
