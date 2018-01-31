APP_ABI := x86 armeabi
#APP_PLATFORM := android-15
#APP_STL := gnustl_static #GNU STL
APP_STL := stlport_static
APP_CPPFLAGS := -fexceptions -frtti 　#允许异常功能，及运行时类型识别
#APP_CPPFLAGS +=-std=c++11             #允许使用c++11的函数等功能
#APP_CPPFLAGS += -frtti                #开启RTTI
#APP_CPPFLAGS +=-fpermissive          #此项有效时表示宽松的编译形式，比如没有用到的代码中有错误也可以通过编译；
                                      #使用GNU STL时不用此项std::string 居然编译不通过！！