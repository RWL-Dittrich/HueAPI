# JHAPI a JavaHueAPI
This is gonna be used for a school project. Feel free to use it anywhere else!

It is still in early development.

# How to use?
###Making a Hue bridge:
```java
HueBridge bridge = new HueBridge("<Hue ip address>", "<Hue user string>", (optional)<TransitionTime>);
```

###Getting the lights:
```java
Collection lights = bridge.getLights();
```

###Methods in a light:
```java
Light light = lights.get(0);

light.setRGB(<R>, <G>, <B>, (optional)<TransitionTime>);
```