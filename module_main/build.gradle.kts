import com.ndhzs.build.logic.depend.dependAndroidKtx
import com.ndhzs.build.logic.depend.dependAndroidView
import com.ndhzs.build.logic.depend.dependLifecycleKtx

plugins {
    id("module-manager")
}

dependAndroidView()
dependAndroidKtx()
dependLifecycleKtx()