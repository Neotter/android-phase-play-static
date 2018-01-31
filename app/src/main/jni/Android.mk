# A simple test for the minimal standard C++ library
#

LOCAL_C_INCLUDES := $(NDK_ROOT)/sources/cxx-stl/stlport/stlport
LOCAL_STATIC_LIBRARIES := $(NDK_ROOT)/sources/cxx-stl/stlport/libs/armeabi/libstlport_static.a

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := PhaseProcess
LOCAL_SRC_FILES += MatrixProcess.cpp RangeFinder.cpp PhaseProcess.cpp
include $(BUILD_EXECUTABLE)