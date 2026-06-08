cmake_minimum_required(VERSION 3.22.1)

# Project name
project(identity_spoof)

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Compiler flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fvisibility=hidden -fPIC -Wl,--exclude-libs,ALL")

# Add the main identity spoof library
add_library(identity_spoof SHARED
    identity_spoof/identity_spoof.cpp
    identity_spoof/device_properties.cpp
    identity_spoof/native_hooks.cpp
    identity_spoof/jni_bindings.cpp
    utils/logger.cpp
    utils/string_utils.cpp
)

# Link against Android libraries
target_link_libraries(identity_spoof
    PRIVATE
    log
    android
    ${ANDROID_LIBRARY}
)

# Include directories
target_include_directories(identity_spoof
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/identity_spoof
    ${CMAKE_CURRENT_SOURCE_DIR}/utils
)

# Optimization flags for release builds
if(CMAKE_BUILD_TYPE STREQUAL "Release")
    target_compile_options(identity_spoof PRIVATE -O3 -DNDEBUG)
else()
    target_compile_options(identity_spoof PRIVATE -g -O0)
endif()