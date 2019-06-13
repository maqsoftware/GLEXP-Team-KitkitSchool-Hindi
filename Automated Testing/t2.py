import os
import cv2
from time import sleep
from appium import webdriver
from pathlib import Path
import numpy as np
from appium.webdriver.common.touch_action import TouchAction



def find_matches(screenshot, template, threshold):
	w, h = template.shape[::-1]
		
	res = cv2.matchTemplate(screenshot,template,cv2.TM_CCOEFF_NORMED)  # @UndefinedVariable
	loc = np.where( res >= threshold )
	count = 0
	points = []
	# Counts the matches themselves
	#and saves their centers to an array
	for pt in zip(*loc[::-1]):
		count = count + 1
		points.append((pt[0]+w/2, pt[1]+h/2))
	if(count == 0):
		threshold = threshold - 0.05
		return find_matches(screenshot, template, threshold)
	print(threshold)
	print(count, " match(es) found.")
	return points

def clicktile(test, temp):
	testimg = cv2.imread(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun", test), 0)
	tempimg = cv2.imread(os.path.join(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp", temp), 0)
	(h, w) = tempimg.shape[:2]
	w = int(w * (4/3))
	h = int(h * (4/3))
	tempimg = cv2.resize(tempimg, (w,h))
	rect = find_matches(testimg,tempimg,0.9)
	TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

def check_games(menu, section, sub_section, game):
	temp_path = string(menu) + '-' + string(section) + '-' + string(sub_section) + '-' + string(game)
	parent = string(menu) + '-' + string(section) + string(sub_section)
	clicktile(parent, temp_path)
	sleep(20)
	driver.save_screenshot(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun", temp_path))
	sleep(2)

def check_sub_section(menu, section, sub_section):
	temp_path = string(menu) + '-' + string(section) + '-' + string(sub_section)
	parent = string(menu) + '-' + string(section)
	clicktile(parent, temp_path)
	sleep(4)
	driver.save_screenshot(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun", temp_path))
	sleep(2)

def check_main_menu(menu):
	temp_path = string(menu)
	parent = "homescreen.png"
	click_tile("homescreen.png", temp_path)
	sleep(4)	
	driver.save_screenshot(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun", temp_path))
	sleep(2)

def check_section(menu, section):
	temp_path = string(menu) + '-' + string(section)
	parent = string(menu)
	clicktile(parent, temp_path)
	sleep(4)
	driver.save_screenshot(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun", temp_path))
	sleep(4)

if __name__ == '__main__':
	desired_caps = {}
	desired_caps['platformName'] = 'Android'
	desired_caps['platformVersion'] = '7.1.1'
	desired_caps['deviceName'] = 'Nexus 6'
	desired_caps['appPackage'] = 'com.maq.xprize.kitkitlauncher.hindi'
	desired_caps['appActivity'] = 'com.maq.xprize.kitkitlauncher.hindi.MainActivity'

	driver = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)

	num_of_ss = [[5, 9, 27, 28, 32, 28, 30, 30, 30, 30, 25], [5, 16, 17, 28, 21, 28, 28, 28, 28, 28, 28]]

	num_of_games = [[[3, 3, 3, 3, 3], 
					 [5, 5, 5, 5, 5, 5, 5, 5, 1], 
					 [4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1], 
					 [4, 4, 4, 4, 5, 4, 4, 4, 5, 4, 4, 4, 5, 4, 5, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1], 
					 [5, 4, 5, 4, 5, 4, 5, 1, 4, 5, 4, 5, 5, 5, 5, 1, 4, 4, 4, 4, 4, 5, 4, 1, 5, 4, 4, 5, 4, 5, 4, 1],
					 [5, 4, 5, 4, 5, 4, 1, 5, 4, 5, 4, 5, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 1],
					 [4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1],
					 [4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1],
					 [4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1],
					 [4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1]],
					[[3, 4, 4, 3, 4],
					 [5, 4, 5, 4, 4, 4, 5, 4, 5, 4, 5, 5, 5, 4, 5, 1],
					 [5, 4, 5, 5, 5, 4, 5, 4, 5, 5, 4, 5, 5, 5, 5, 5, 1],
					 [5, 5, 5, 6, 5, 6, 1, 6, 6, 5, 5, 5, 6, 1, 6, 6, 5, 5, 6, 5, 1, 5, 5, 6, 5, 5, 5, 1],
					 [5, 5, 5, 5, 5, 4, 1, 5, 5, 5, 5, 5, 4, 1, 5, 5, 5, 5, 5, 5, 1],
					 [5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 4, 5, 5, 5, 5, 1],
					 [5, 5, 6, 5, 5, 5, 1, 5, 6, 5, 6, 5, 5, 1, 5, 5, 5, 5, 5, 6, 1, 5, 5, 4, 5, 5, 5, 1],
					 [5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1],
					 [5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1],
					 [5, 5, 5, 5, 5, 4, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 1]]]

	# d = Device('9aa84043', adb_server_host='0.0.0.0', adb_server_port='4723')
	driver.find_element_by_android_uiautomator('text("ALLOW")').click()
	sleep(1)
	driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\launcherscreen.png")
	sleep(2)
	clicktile("launcherscreen.png", "maintile.png")

	sleep(20)

	driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\homescreen.png")
	sleep(2)

	for menu in range(2):
		check_main_menu(menu)
		for section in range(10):
			check_section(menu, section)
			for sub_section in range(num_of_ss[menu][section]):
				check_sub_section(menu, section, sub_section)
				for game in range(num_of_games[menu][section][sub_section]):
					check_games(menu, section, sub_section, game)
	
	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\homescreen.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\PixelXL\temp\homeabctile.png",0)
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(4)	
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\alpha.png")
	# sleep(2)

	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\alpha.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\OnePlus2\temp\chidiya1.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))

	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(4)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\icmenu.png")
	# sleep(4)

	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\icmenu.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\OnePlus2\temp\1.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()


	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\icmenu.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\OnePlus2\temp\2.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# driver.back()
	# sleep(1)
	# driver.back()
	# sleep(1)
	# driver.back()

	# sleep(2)
	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\homescreen.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\home123tile.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(4)	
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\numerics.png")
	# sleep(2)

	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\numerics.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\liz.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(4)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\ilmenu.png")
	# sleep(4)

	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\ilmenu.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\ilt.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(4)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\tv.png")
	# sleep(2)

	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\tv.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\bal.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(20)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\bg.png")
	# sleep(4)

	# driver.back()

	# sleep(4)
	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\tv.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\ins.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(20)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\ig.png")
	# sleep(4)

	# driver.back()

	# sleep(4)
	# testimg = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\tv.png",0)
	# tempimg = cv2.imread(r"C:\Users\MAQUser\Documents\temp\Screenshots\OnePlus2\temp\num.png",0)
	# (h, w) = tempimg.shape[:2]
	# w = int(w * (4/3))
	# h = int(h * (4/3))
	# tempimg = cv2.resize(tempimg, (w,h))
	# rect = find_matches(testimg,tempimg,0.9)
	# TouchAction(driver).tap(None, rect[0][0], rect[0][1], 0.5).perform()

	# sleep(20)
	# driver.save_screenshot(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus 6\testrun\ng.png")
	# sleep(4)

	# driver.back()
	# sleep(1)
	# driver.back()
	# sleep(1)
	# driver.back()
	# sleep(1)
	# driver.back()

	# sleep(1)
	# driver.back()

