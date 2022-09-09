import com.ndhzs.build.logic.depend.*

plugins {
    id("module-manager")
    id("kotlin-parcelize")
}

dependAndroidView()
dependAndroidKtx()
dependLifecycleKtx()
dependNetwork()
dependNavigation()
dependGlide()
dependRxjava()
dependRoom()

dependencies {
    // Toasty
    implementation("com.github.GrenderG:Toasty:1.5.2")
}