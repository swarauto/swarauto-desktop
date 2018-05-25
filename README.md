**SWarAuto** is a smart tool that supports auto farming for game Summoners War: Sky Arena.
  - Auto farm Cairos Dungeon, Faimon, TOA, Rift beast.  
  - Auto refill energy. Retry on network problems.  
  - Custom auto sell rune drop by rarity.  
  - Smart recognize game screen to give directives, prevent detection.  

**swarauto-desktop** is desktop version of SWarAuto. Support Window, Mac, or Linux.

---

# Requirements
  - Supported OS: Windows/Linux/Mac
  - An Android Device

# How to use

## 1. Preparation
  - Install JAVA 8 to your desktop. Make sure install correct version and architecture (32-bit or 64-bit)  
  - Enable **Developer Mode** and **USB debugging** in Android  
  - Run SWarAuto:  
    - For Windows: execute `run.win.bat`  
    - For Linux: execute `run.linux.sh`  
    - For MAC: execute `run.mac.command`  
  - Connect Android to Desktop by USB cable. **Allow USB debugging** if asked. Make sure SWarAuto window show **Device: CONNECTED** in green.  

## 2. Configure device
  - In SWarAuto window, click button **New Device**, named your device as you want.  
  - Click **Run Device Auto Config** and following guidance (work best for 16:9 device).  
  - In lower sections, configure all remain RED items, until all items are GREEN and OK shown.  

## 3. Start autoing
  - Now you device is ready for Auto  
  - Choose your scenario, max refills, max runs...  
  - Click Start.  

# Dependencies
  - SWarAuto is inspired by [sw-bot](https://github.com/justindannguyen/sw-bot)
  - Use OpenCV for game screen recognition
  - Use base lib [swarauto-base](https://github.com/swarauto/swarauto-base)

# Disclaimer  
**This is personal fun project, please use it as your own risks. We are not responsible for any banned account**
