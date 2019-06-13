import os
import cv2 
image = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus6\testrun\tv.png")
gray=cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)
edged = cv2.Canny(image, 10, 250)
(cnts, _) = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
idx = 0
for c in cnts:
	x,y,w,h = cv2.boundingRect(c)
	if w>50 and h>50:
		idx+=1
		new_img=image[y:y+h,x:x+w]
		cv2.imwrite(os.path.join(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus6\testrun", str(idx)) + '.png', new_img)
cv2.imshow("im",image)
cv2.waitKey(0)

# import cv2
# #reading the image 
# image = cv2.imread(r"C:\Users\MAQUser\Documents\workspace\Screenshots\Nexus6\testrun\icmenu.png")
# edged = cv2.Canny(image, 10, 250)
# cv2.imshow("Edges", edged)
# cv2.waitKey(0)
 
# #applying closing function 
# kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (7, 7))
# closed = cv2.morphologyEx(edged, cv2.MORPH_CLOSE, kernel)
# cv2.imshow("Closed", closed)
# cv2.waitKey(0)
 
# #finding_contours 
# (cnts, _) = cv2.findContours(closed.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
 
# for c in cnts:
# 	peri = cv2.arcLength(c, True)
# 	approx = cv2.approxPolyDP(c, 0.02 * peri, True)
# 	cv2.drawContours(image, [approx], -1, (0, 255, 0), 2)
# cv2.imshow("Output", image)
# cv2.waitKey(0)