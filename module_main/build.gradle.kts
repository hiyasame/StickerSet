import com.ndhzs.build.logic.depend.*

plugins {
    id("module-manager")
}

dependAndroidView()
dependAndroidKtx()
dependLifecycleKtx()
dependNetwork()
dependNavigation()
dependGlide()
dependRxjava()
dependRoom()