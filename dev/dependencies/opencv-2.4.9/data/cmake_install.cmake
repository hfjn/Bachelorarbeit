# Install script for directory: /Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/usr/local")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Release")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "libs")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/OpenCV/haarcascades" TYPE FILE FILES
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_eye.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_eye_tree_eyeglasses.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_frontalface_alt.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_frontalface_alt2.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_frontalface_alt_tree.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_frontalface_default.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_fullbody.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_lefteye_2splits.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_lowerbody.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_eyepair_big.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_eyepair_small.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_leftear.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_lefteye.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_mouth.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_nose.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_rightear.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_righteye.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_mcs_upperbody.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_profileface.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_righteye_2splits.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_smile.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/haarcascades/haarcascade_upperbody.xml"
    )
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "libs")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/OpenCV/lbpcascades" TYPE FILE FILES
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/lbpcascades/lbpcascade_frontalface.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/lbpcascades/lbpcascade_profileface.xml"
    "/Volumes/HDD/Jannik/Downloads/opencv-2.4.9/data/lbpcascades/lbpcascade_silverware.xml"
    )
endif()

