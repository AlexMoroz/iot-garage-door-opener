from grovepi import *
import RPi.GPIO as GPIO
import requests
import time


red = 3
green = 4
pinMode(red, "OUTPUT")
pinMode(green, "OUTPUT")
time.sleep(1)
# to use Raspberry Pi board pin numbers
GPIO.setmode(GPIO.BCM)
# set up GPIO output channel
GPIO.setup(4, GPIO.OUT)
GPIO.setup(17, GPIO.OUT)

saved = requests.get("http://teamzero.azurewebsites.net/api/status").json()['open']
print(saved)

def blink(led):
        digitalWrite(led,1)             # Send HIGH to switch on LED
        time.sleep(1)
        digitalWrite(led,0)             # Send LOW to switch off LED
        time.sleep(1)
        return

while True:
        data = requests.get("http://teamzero.azurewebsites.net/api/status").json()
        print(data)
        status = data["open"]
        alarm = data["alarm"]
        if status != saved :
                # blink 3 times
                if status == True:
                        for x in range(0,3):
                             blink(green)
                saved = status
                if status == True:
                        GPIO.output(4, GPIO.HIGH)
                else:
                        GPIO.output(17, GPIO.HIGH)
        else:
                GPIO.output(4, GPIO.LOW)
                GPIO.output(17, GPIO.LOW)
        if alarm == True:
             requests.post("http://teamzero.azurewebsites.net/api/offalarm")
             # blink 3 times
             for x in range(0,3):
                     blink(red)