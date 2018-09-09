# ZXing
二维码扫描 和 生成二维码
# gradle 配置
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  dependencies {
	        implementation 'com.github.Wiser-Wong:ZXing:1.0.0'
	}
