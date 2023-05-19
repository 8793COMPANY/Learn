/*
 *  Copyright 2015 Google Inc. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @fileoverview Generators for the Turtle Blockly demo on Android.
 * @author fenichel@google.com (Rachel Fenichel)
 */
'use strict';

// Extensions to Blockly's language and JavaScript generator.

Blockly.JavaScript['type_string'] = function(block) {
  var value_string = block.getFieldValue("STRING_TEXT");
  return ['"' + value_string + '"', Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['type_int'] = function(block) {
  var value_int = block.getFieldValue('INT_TEXT');
  return [value_int, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['type_boolean'] = function(block) {
  var value_boolean = block.getFieldValue('BOOLEAN_TEXT');
  return [value_boolean, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['serial_print'] = function(block) {
  var value_text = Blockly.JavaScript.valueToCode(block, "text", Blockly.JavaScript.ORDER_ATOMIC);
  var code = "Serial.print("+value_text+");\n";
  return code;
};

Blockly.JavaScript['serial_println'] = function(block) {
  var value_text = Blockly.JavaScript.valueToCode(block, "STRING", Blockly.JavaScript.ORDER_ATOMIC);
  var code = "Serial.println("+value_text+");\n";
  return code;
};

Blockly.JavaScript['serial_println_int'] = function(block) {
  var value_num = Blockly.JavaScript.valueToCode(block, "INT", Blockly.JavaScript.ORDER_ATOMIC);
  var code = "Serial.println("+value_num+");\n";
  return code;
};

Blockly.JavaScript['serial_println_boolean'] = function(block) {
  var value_num = Blockly.JavaScript.valueToCode(block, "BOOLEAN", Blockly.JavaScript.ORDER_ATOMIC);
  var code = "Serial.println("+value_num+");\n";
  return code;
};

Blockly.JavaScript['turtle_move_internal'] = function(block) {
  // Generate JavaScript for moving forward or backwards.
  var value = block.getFieldValue('VALUE');
  return 'Turtle.' + block.getFieldValue('DIR') +
      '(' + value + ', \'block_id_' + block.id + '\');\n';
};


Blockly.JavaScript['turtle_color_internal'] = function(block) {
  // Generate JavaScript for moving forward or backwards.

  return "";
};

Blockly.JavaScript['turtle_jikco_internal'] = function(block) {
  // Generate JavaScript for moving forward or backwards.
  var value = block.getFieldValue('DIR');
  var color_text = block.getFieldValue('COL');

  var color = "";

  if(color_text == 'Red'){
    color = "0xff0000";
  }else if(color_text == 'Orange'){
    color = "0xff8c00";
  }else if(color_text == 'Yellow'){
    color = "0xffff00";
  }else if(color_text == 'Green'){
    color = "0x008000";
  }else if(color_text == 'Blue'){
    color = "0x0000ff";
  }else if(color_text == 'Brown'){
    color = "0x964B00";
  }else if(color_text == 'Purple'){
    color = "0x800080";
  }else if(color_text == 'Off'){
    color = "0x000000";
  }else if(color_text == 'White'){
    color = "0xffffff";
  }



  var code ="";
  const checked = value.split(',');

  if(checked[0] == "1"){
  code += "neo.setPixelColor(3,"+color+");\n";
  }

  if(checked[1]  == "1"){
    code += "neo.setPixelColor(4,"+color+");\n";
  }

  if(checked[2]  == "1"){
    code += "neo.setPixelColor(5,"+color+");\n";
  }

  if(checked[3] == "1"){
    code += "neo.setPixelColor(9,"+color+");\n";
  }

  if(checked[4] == "1"){
     code += "neo.setPixelColor(10,"+color+");\n";
  }
    if(checked[5] == "1"){
       code += "neo.setPixelColor(11,"+color+");\n";
    }
    if(checked[6] == "1"){
       code += "neo.setPixelColor(0,"+color+");\n";
    }
    if(checked[7] == "1"){
       code += "neo.setPixelColor(1,"+color+");\n";
    }
    if(checked[8] == "1"){
       code += "neo.setPixelColor(2,"+color+");\n";
    }
  if(checked[9] == "1"){
     code += "neo.setPixelColor(6,"+color+");\n";
  }

  if(checked[10] == "1"){
     code += "neo.setPixelColor(7,"+color+");\n";
  }

  if(checked[11] == "1"){
    code += "neo.setPixelColor(8,"+color+");\n";
  }

   code += "\nneo.show();\n";

  return code;
};

Blockly.JavaScript['turtle_turn_internal'] = function(block) {
  // Generate JavaScript for turning left or right.
  var value = block.getFieldValue('VALUE');
  return 'Turtle.' + block.getFieldValue('DIR') +
      '(' + value + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['set_leds'] = function (block) {
 var value1 = block.getFieldValue('VALUE1');
 var value2 = block.getFieldValue('VALUE2');``````````````````````````
 var value3 = block.getFieldValue('VALUE3');

    return 'pinMode(11, OUTPUT);\npinMode(12, OUTPUT);\npinMode(13, OUTPUT);\ndigitalWrite(11,'+value1+');\ndigitalWrite(12,'+value2+');\ndigitalWrite(13,'+value3+');\n';
};

Blockly.JavaScript['pinMode'] = function (block) {
   var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   var value_num = Blockly.JavaScript.valueToCode(block, "VALUE1", Blockly.JavaScript.ORDER_ATOMIC);
   var code = "\npinMode(" + value_pin + ", " + value_num + ");\n";
   return code;
};

Blockly.JavaScript['set_led'] = function (block) {
     var value1 = block.getFieldValue('VALUE1');
      var value_pin = block.getFieldValue('PIN');

//    var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);

    return '\npinMode(13,'+value1+');\n';
};

Blockly.JavaScript['dc_motor'] = function(block) {
  // Generate JavaScript for setting the width.
  var channel = parseInt(block.getFieldValue('channel'));
  var speed = parseInt(Blockly.JavaScript.valueToCode(block, 'speed',Blockly.JavaScript.ORDER_NONE) || '255');
   if( speed > 255 || speed < -255) {
     return '!!alert!!DC motor : speed should be between -255 and 255!!\n';
   }

    var code;
    Blockly.JavaScript.setups_['setup_pin_mode_4'] =  "\npinMode(4, OUTPUT);";
    Blockly.JavaScript.setups_['setup_pin_mode_2'] = "\npinMode(2, OUTPUT);";
    Blockly.JavaScript.setups_['setup_pin_mode_7'] = "\npinMode(7, OUTPUT);";
    Blockly.JavaScript.setups_['digital_write_7'] = "\ndigitalWrite(7,LOW);";
    Blockly.JavaScript.setups_['digital_write_3'] = "\ndigitalWrite(2,HIGH);";
    Blockly.JavaScript.setups_['setup_pin_mode_6'] = "\npinMode(6, OUTPUT);";

    if ( channel == 1) {
  	  if (speed >= 0 )
          code = "digitalWrite(4,LOW);\n";
  	  else
  		code = "digitalWrite(4,HIGH);\n";
      code += "analogWrite(3,"+speed+");\n";
    }
    else if(channel == 2) {
  	   if (speed >= 0 )
          code = "digitalWrite(6,LOW);\n";
  	  else
  		code = "digitalWrite(6,HIGH);\n";
      code += "analogWrite(5,"+speed+");\n";
    }
    return code;
};

Blockly.JavaScript['lcd'] = function (block) {
  var text = Blockly.JavaScript.valueToCode(block, 'text', Blockly.JavaScript.ORDER_NONE) || '255';
  var line_number = block.getFieldValue('line_number');
  var character_number = parseInt(Blockly.JavaScript.valueToCode(block, 'character_number', Blockly.JavaScript.ORDER_NONE) || '0');
  // Assemble JavaScript into code variable.
  //import lcd
  //return 'text-'+text+'line-'+line_number+'char-'+character_number+'\n';
  if( character_number < 0  || character_number > 15 ) {
    return '!!alert!!LCD : character should be between 0 to 15!!\n';
  }
  Blockly.JavaScript.setups_["%1"] = "\n lcd.begin(16, 2);";
  Blockly.JavaScript.definitions_["includelib"] = "#include <LiquidCrystal.h>";
  Blockly.JavaScript.definitions_["definelcdpins"] = "LiquidCrystal lcd(8,9,10,11,12,13);"
  var code = 'lcd.setCursor(' + character_number + ',' + line_number + ');\n';
  code = code + 'lcd.print(' + text + ');\n'
  return code;
};

Blockly.JavaScript['clear_lcd'] = function (block) {
  //  Assemble JavaScript into code variable.
  var code = 'lcd.clear();\n';
  return code;
};

Blockly.JavaScript['bluetooth_sensor'] = function (block) {
  var baudrate = Blockly.JavaScript.valueToCode(block, 'baud', Blockly.JavaScript.ORDER_NONE) || '255';
  //define bluetooth settings
  Blockly.JavaScript.setups_['setup_bluetooth'] = "\n Serial.begin("+baudrate+");";
  Blockly.JavaScript.definitions_['define_bluetooth'] = "char readBluetooth()\n{\n while(Serial.available())\n {\n char inChar = (char)Serial.read();\n return inChar;\n}\n}\n";
    var code = 'readBluetooth()';

    return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['sonar_sensor'] = function (block) {
  var value_trig = Blockly.JavaScript.valueToCode(block, 'trig', Blockly.JavaScript.ORDER_NONE);
  var value_echo = Blockly.JavaScript.valueToCode(block, 'echo', Blockly.JavaScript.ORDER_NONE);
  //define sonar settings
  Blockly.JavaScript.definitions_['define_sonar'] = "int readUltrasonic_cm(int trigPin, int echoPin)\n{ \n pinMode(trigPin, OUTPUT);\n digitalWrite(trigPin, LOW);\n delayMicroseconds(2);\n digitalWrite(trigPin, HIGH);\n delayMicroseconds(10);\n digitalWrite(trigPin, LOW);\n pinMode(echoPin, INPUT);\n return pulseIn(echoPin, HIGH)/ 29 / 2;\n}\n";
  //  Assemble Arduino into code variable.
  var code = "readUltrasonic_cm("+value_trig+","+value_echo+")";

  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['remote_sensor'] = function (block) {
  var value_tsop = Blockly.JavaScript.valueToCode(block, 'tsop', Blockly.JavaScript.ORDER_NONE);
  Blockly.JavaScript.definitions_['define_remote'] = "int remote(int pinNumber)\n{\nint value = 0;\nint time = pulseIn(pinNumber,LOW);\n if(time>2000)\n{\nfor(int counter1=0;counter1<12;counter1++)\n{\nif(pulseIn(pinNumber,LOW)>1000)\n{\nvalue = value + (1<< counter1);\n }\n}\n}\n return value;\n}\n";
  var code = "remote("+value_tsop+")";
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['servo'] = function (block) {
  var value_channel = Blockly.JavaScript.valueToCode(block, 'channel', Blockly.JavaScript.ORDER_ATOMIC);
  var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

  //define sonar settings
  Blockly.JavaScript.definitions_['define_servo_h'] = "#include <Servo.h>\n";
  Blockly.JavaScript.definitions_['define_servo_' + value_channel] = "Servo servo" + value_channel + ";\n";

  Blockly.JavaScript.setups_['define_servo' + value_channel] = '\n servo' + value_channel + '.attach('+value_channel+');\n';
  if( (value_angle < 0 ) || (value_angle > 180 )) {
     return '!!alert!!Servo : angle should be between 0 and 180!!\n';
  }
    // Assemble JavaScript into code variable.
    var code = 'servo' + value_channel + '.write(' + value_angle + ');\n';
    return code;
 };

 Blockly.JavaScript['neo_pixel'] = function (block) {
   var value_channel = Blockly.JavaScript.valueToCode(block, 'channel', Blockly.JavaScript.ORDER_ATOMIC);
   var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);
//   var neo_name = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

   //define sonar settings
   Blockly.JavaScript.definitions_['define_neo_h'] = "#include <Adafruit_NeoPixel.h>\n";
   Blockly.JavaScript.definitions_['define_neo_' + value_channel] = "Adafruit_NeoPixel neo"+" = Adafruit_NeoPixel(" + value_channel + ","+value_angle+", NEO_GRB + NEO_KHZ800)"+";\n";

     var code = "";
     return code;
  };

  Blockly.JavaScript['neo_pixel_setup'] = function (block) {


     Blockly.JavaScript.setups_['define_neo_setup'] =  " neo.begin();"+
                   "\nneo.setBrightness(20);"+
                   "\nneo.setPixelColor(0, 0xff0000);"+
                   "\nneo.setPixelColor(2, 0xff0000);"+
                   "\nneo.setPixelColor(4, 0xff0000);"+
                   "\nneo.setPixelColor(6, 0xff0000);"+
                   "\nneo.setPixelColor(8, 0xff0000);"+
                   "\nneo.setPixelColor(10, 0xff0000);"+
                   "\nneo.show();"+
                   "\nneo.setPixelColor(0, 0xff0000);"+
                   "\nneo.setPixelColor(2, 0xff0000);"+
                   "\nneo.setPixelColor(4, 0xff0000);"+
                   "\nneo.setPixelColor(6, 0xff0000);"+
                   "\nneo.setPixelColor(8, 0xff0000);"+
                   "\nneo.setPixelColor(10, 0xff0000);"+
                   "\nneo.show();"+
                   "\nneo.clear();"+
                   "\nneo.show();\n";

       var code = ""

       return code;
    };

        Blockly.JavaScript['neo_pixel_begin'] = function (block) {

           var code = "neo.begin();\n"

           return code;
        };

      Blockly.JavaScript['neo_pixel_brightness'] = function (block) {
       var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

           var code = "neo.setBrightness("+ value_angle + ");\n"

           return code;
        };

  Blockly.JavaScript['face_list'] = function() {
    var dropdown_value = this.getFieldValue('face');
    return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
  };

  Blockly.JavaScript['color_list'] = function() {
      var dropdown_value = this.getFieldValue('color');
      return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
  };

Blockly.JavaScript['neo_pixel_custom'] = function (block) {
   var value_face = Blockly.JavaScript.valueToCode(block, 'face', Blockly.JavaScript.ORDER_ATOMIC);
   var value_color = Blockly.JavaScript.valueToCode(block, 'color', Blockly.JavaScript.ORDER_ATOMIC);

   var code = "";
   var code_text = "";
   var set_color = "";
   var light_num = [];

   switch(value_color) {
     case 'Red' : set_color = "0xff0000"; break;
     case 'Orange' : set_color = "0xff8c00"; break;
     case 'Yellow' : set_color = "0xffff00"; break;
     case 'Green' : set_color = "0x008000"; break;
     case 'Blue' : set_color = "0x0000ff"; break;
     case 'Brown' : set_color = "0x964B00"; break;
     case 'Purple' : set_color = "0x800080"; break;
     case 'Off' : set_color = "0x000000"; break;
     case 'White' : set_color = "0xffffff"; break;
   }

   switch(value_face) {
     case 'happy' : light_num = ['0', '2', '4', '6', '8', '10']; break;
     case 'sad' : light_num = ['1', '3', '4', '5', '7', '9', '10', '11']; break;
     case 'sulk' : light_num = ['0', '1', '2', '6', '7', '8']; break;
     case 'thrilling' : light_num = ['1', '3', '5', '7', '9', '11']; break;
     case 'amazed' : light_num = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11']; break;
     case 'sleepy' : light_num = ['0', '1', '2', '3', '5', '6', '7', '8', '9', '11']; break;
     case 'smile' : light_num = ['0', '2', '3', '4', '5', '6', '8', '9', '10', '11']; break;
     case 'vacant' : light_num = ['0', '1', '3', '4', '7', '8', '10', '11']; break;
     case 'upset' : light_num = ['2', '3', '4', '6', '10', '11']; break;
   }

   for(var i=0; i<light_num.length; i++) {
     code_text = code_text + "\nneo.setPixelColor(" + light_num[i] + "," + set_color + ");";
   }

   code = "\nneo.clear();"+
          "\nneo.show();" +
          code_text +
          "\nneo.show();\n";

   return code;
  };

 Blockly.JavaScript['neo_pixel_color'] = function (block) {
   var value_channel = Blockly.JavaScript.valueToCode(block, 'channel', Blockly.JavaScript.ORDER_ATOMIC);
   var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);
//   var neo_name = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

     var code = "neo.setPixelColor("+value_channel+","+"0xff0000);\n";
     return code;
  };

   Blockly.JavaScript['neo_pixel_show'] = function (block) {
  //   var neo_name = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

       var code = "neo.show();\n";
       return code;
    };

    //neo_pixel_clear
    Blockly.JavaScript['neo_pixel_clear'] = function (block) {
      //   var neo_name = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

           var code = "neo.clear();\n";
           return code;
        };

Blockly.JavaScript['logic_calculation'] = function(a) {
    var b = {
            EQ: "+",
            LT: "-",
            GT: "*",
            GTE: "/"
        }[a.getFieldValue("OP")],
        c = "==" == b || "!=" == b ? Blockly.JavaScript.ORDER_EQUALITY : Blockly.JavaScript.ORDER_RELATIONAL,
        d = Blockly.JavaScript.valueToCode(a, "A", c) || "0";
    a = Blockly.JavaScript.valueToCode(a, "B", c) || "0";
    return [d + " " + b + " " + a, c]
};

 Blockly.JavaScript['tone_notone'] = function (block) {
   var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);
   var delay_time = Blockly.JavaScript.valueToCode(block, 'DELAY_TIME', Blockly.JavaScript.ORDER_ATOMIC) || '1000'

     // Assemble JavaScript into code variable.
     var code = "tone(" + value_pin + ", " + value_num + ");\ndelay("+ delay_time + ');\n'+"noTone("+value_pin+");\n";
     return code;
  };

 Blockly.JavaScript['DHT11'] = function (block) {
   var value_channel = Blockly.JavaScript.valueToCode(block, 'channel', Blockly.JavaScript.ORDER_ATOMIC);
//   var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

   //define sonar settings
   Blockly.JavaScript.definitions_['define_dht_h'] = "#include \"DHT.h\"\n";
//   Blockly.JavaScript.definitions_['define_dht_type_h'] = "#include <DHT.h>\n";
   Blockly.JavaScript.definitions_['define_dht_' + value_channel] = "DHT dht(" + value_channel +", DHT11);\n";
    Blockly.JavaScript.setups_['setup_dht_' + value_channel] =  "\ndht.begin();";

//   Blockly.JavaScript.setups_['define_servo' + value_channel] = '\n servo' + value_channel + '.attach('+value_channel+');\n';
//   if( (value_angle < 0 ) || (value_angle > 180 )) {
//      return '!!alert!!Servo : angle should be between 0 and 180!!\n';
//   }
//     // Assemble JavaScript into code variable.
//     var code = 'dht.readTemperature()' + '\n';
     return '';
  };

   Blockly.JavaScript['read_temp'] = function (block) {

  //   Blockly.JavaScript.setups_['define_servo' + value_channel] = '\n servo' + value_channel + '.attach('+value_channel+');\n';
  //   if( (value_angle < 0 ) || (value_angle > 180 )) {
  //      return '!!alert!!Servo : angle should be between 0 and 180!!\n';
  //   }
  //     // Assemble JavaScript into code variable.
       var code = 'dht.readTemperature()';
       return [code, Blockly.JavaScript.ORDER_ATOMIC];
    };

   Blockly.JavaScript['read_humidity'] = function (block) {
//     var value_channel = Blockly.JavaScript.valueToCode(block, 'channel', Blockly.JavaScript.ORDER_ATOMIC);
  //   var value_angle = Blockly.JavaScript.valueToCode(block, 'angle', Blockly.JavaScript.ORDER_ATOMIC);

     //define sonar settings
//     Blockly.JavaScript.definitions_['define_dht_h'] = "#include <DHT.h>\n";
//  //   Blockly.JavaScript.definitions_['define_dht_type_h'] = "#include <DHT.h>\n";
//     Blockly.JavaScript.definitions_['define_dht_' + value_channel] = "DHT dht(" + value_channel +", DHT11);\n";

  //   Blockly.JavaScript.setups_['define_servo' + value_channel] = '\n servo' + value_channel + '.attach('+value_channel+');\n';
  //   if( (value_angle < 0 ) || (value_angle > 180 )) {
  //      return '!!alert!!Servo : angle should be between 0 and 180!!\n';
  //   }
  //     // Assemble JavaScript into code variable.
       var code = 'dht.readHumidity()';
       return [code, Blockly.JavaScript.ORDER_ATOMIC];
    };

Blockly.JavaScript['inout_tone_pin'] = function(block) {
   var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);
   Blockly.JavaScript.setups_['setup_output'+value_pin] = '\n pinMode('+value_pin+', OUTPUT);';
   var code = "tone(" + value_pin + ", " + value_num + ");\n";
   return code;
 };

Blockly.JavaScript['inout_notone_pin'] = function(block) {
   var dropdown_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   Blockly.JavaScript.setups_['setup_output'+dropdown_pin] = '\n pinMode('+dropdown_pin+', OUTPUT);';
   var code = "noTone(" + dropdown_pin + ");\n";
   return code;
 };

Blockly.JavaScript['inout_digital_write'] = function(block) {
   var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);
   var code = "digitalWrite(" + value_pin + ", " + value_num + ");\n";
   return code;
 };

Blockly.JavaScript['inout_analog_write'] = function(block) {
    var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
    var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);
     if( (value_num < 0 ) || (value_num > 255 )) {
         return '!!alert!!Pin : analog value should be between 0 and 255!!\n';
      }
    //var code = "pinMode("+value_pin+", OUTPUT);\n analogWrite(" + value_pin + ", " + value_num + ");\n";
    var code = "analogWrite(" + value_pin + ", " + value_num + ");\n";
    return code;
  };

Blockly.JavaScript['inout_digital_read'] = function(block) {
  var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
//  Blockly.JavaScript.definitions_['digital_read'] = "int digRead(int pinNumber)\n{\n pinMode("+value_pin+", INPUT);\n return digitalRead(" + value_pin + ");\n}\n"
  var code = "digitalRead(" + value_pin + ")";
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['serial_begin_list'] = function(block) {
    //var num = parseInt(Blockly.JavaScript.valueToCode(block, 'SB',Blockly.JavaScript.ORDER_NONE));

    var dropdown_value = this.getFieldValue('SB');
    return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
};

//Blockly.JavaScript['number'] = function(block) {
//  // Generate JavaScript for setting the width.
////  var channel = parseInt(block.getFieldValue('channel'));
//    var num = parseInt(Blockly.JavaScript.valueToCode(block, 'num',Blockly.JavaScript.ORDER_NONE) || '255');
//
//    return num;
//};

//Blockly.JavaScript['inout_analog_read'] = function(block) {
//  var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
//  Blockly.JavaScript.definitions_['analog_read'] = "int anaRead(int pinNumber)\n{\n pinMode("+value_pin+", INPUT);\n return analogRead(" + value_pin + ");\n}\n"
//    var code = "anaRead(" + value_pin + ")";
//    return [code, Blockly.JavaScript.ORDER_ATOMIC];
//};

Blockly.JavaScript['inout_analog_read'] = function(block) {
  var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
//  Blockly.JavaScript.definitions_['analog_read'] = "int anaRead(int pinNumber)\n{\n pinMode("+value_pin+", INPUT);\n return analogRead(" + value_pin + ");\n}\n"
    var code = "analogRead(" + value_pin + ")";
   return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

/*Blockly.JavaScript['serial_print'] = function(block) {
   var value_baud = Blockly.JavaScript.valueToCode(block, "baud", Blockly.JavaScript.ORDER_ATOMIC);
   var value_text = Blockly.JavaScript.valueToCode(block, "text", Blockly.JavaScript.ORDER_ATOMIC);
//   Blockly.JavaScript.setups_['setup_serial_print'] = '\n Serial.begin('+value_baud+');';
   var code = "Serial.print("+value_text+");\n";
   return code;
 };*/

 Blockly.JavaScript['variables_change'] = function() {
   // Variable setter.
   var varValue = Blockly.JavaScript.valueToCode(this, 'VALUE',
       Blockly.JavaScript.ORDER_ASSIGNMENT) || '0';
   var varName = Blockly.JavaScript.variableDB_.getName(this.getFieldValue('VAR'),
       Blockly.Variables.NAME_TYPE);

   return varName + ' = ' + varValue + ';\n';
 };

Blockly.JavaScript['serial_begin'] = function(block) {
    //Blockly.JavaScript.definitions_['define_DHT11_h'] = "#include <DHT.h>\n";

   var value_baud = Blockly.JavaScript.valueToCode(block, "baud", Blockly.JavaScript.ORDER_ATOMIC);
   var code = "\nSerial.begin("+value_baud+");\n";
   return code;
 };

/* Blockly.JavaScript['serial_println'] = function(block) {
    var value_baud = Blockly.JavaScript.valueToCode(block, "baud", Blockly.JavaScript.ORDER_ATOMIC);
    var value_text = Blockly.JavaScript.valueToCode(block, "text", Blockly.JavaScript.ORDER_ATOMIC);
//    Blockly.JavaScript.setups_['setup_serial_print'] = '\n Serial.begin('+value_baud+');';
    var code = "Serial.println("+value_text+");\n";
    return code;
  };*/

   Blockly.JavaScript['random_number'] = function(block) {
      var from_num = Blockly.JavaScript.valueToCode(block, "from_num", Blockly.JavaScript.ORDER_ATOMIC);
      var to_num = Blockly.JavaScript.valueToCode(block, "to_num", Blockly.JavaScript.ORDER_ATOMIC);
  //    Blockly.JavaScript.setups_['setup_serial_print'] = '\n Serial.begin('+value_baud+');';
      var code = "random("+from_num+","+to_num+");";
      return code;
    };

  Blockly.JavaScript['map_number'] = function(block) {

           var variable = Blockly.JavaScript.valueToCode(block, "var", Blockly.JavaScript.ORDER_ATOMIC);
           var in_min = Blockly.JavaScript.valueToCode(block, "in_min", Blockly.JavaScript.ORDER_ATOMIC);
           var in_max = Blockly.JavaScript.valueToCode(block, "in_max", Blockly.JavaScript.ORDER_ATOMIC);
           var out_min = Blockly.JavaScript.valueToCode(block, "out_min", Blockly.JavaScript.ORDER_ATOMIC);
           var out_max = Blockly.JavaScript.valueToCode(block, "out_max", Blockly.JavaScript.ORDER_ATOMIC);

//      var functionName = Blockly.JavaScript.provideFunction_(
//          'map_number',
//          [ 'int ' + Blockly.JavaScript.FUNCTION_NAME_PLACEHOLDER_ + '(int x, int in_min, int in_max, int out_min, int out_max) {',
//
//            '  return map(x,in_min,in_max,out_min,out_max);',
//            '}']);
      // Generate the function call for this block.
//      var code = functionName + '(' + variable+','+in_min +','+in_max+','+out_min +','+out_max + ')';
      var code =  'map(' + variable+','+in_min +','+in_max+','+out_min +','+out_max + ')';
      return [code, Blockly.JavaScript.ORDER_ATOMIC];
        };

/*   Blockly.JavaScript['serial_println_int'] = function(block) {
      var value_baud = Blockly.JavaScript.valueToCode(block, "baud", Blockly.JavaScript.ORDER_ATOMIC);
      var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);
  //    Blockly.JavaScript.setups_['setup_serial_print'] = '\n Serial.begin('+value_baud+');';
      var code = "Serial.println("+value_num+");\n";
      return code;
    };*/

Blockly.JavaScript['base_pins_list'] = function() {
  var dropdown_value = this.getFieldValue('PIN');
  return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['base_logic_list'] = function() {
  var dropdown_value = this.getFieldValue('LOGIC');
  return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['base_output_list'] = function() {
  var dropdown_value = this.getFieldValue('LOGIC');
  return [dropdown_value, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['base_delay'] = function(block) {
  var delay_time = Blockly.JavaScript.valueToCode(block, 'DELAY_TIME', Blockly.JavaScript.ORDER_ATOMIC) || '1000'
  var code = 'delay(' + delay_time + ');\n';
  return code;
};

Blockly.JavaScript['pulseIn'] = function(block) {
   var value_pin = Blockly.JavaScript.valueToCode(block, "PIN", Blockly.JavaScript.ORDER_ATOMIC);
   var value_num = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_ATOMIC);

   var code = "pulseIn(" + value_pin + ", " + value_num + ")";
   return [code, Blockly.JavaScript.ORDER_ATOMIC];
 };

Blockly.JavaScript['turtle_colour_internal'] = function(block) {
  // Generate JavaScript for setting the colour.
  var colour = block.getFieldValue('COLOUR');
  return 'Turtle.penColour(\'' + colour + '\', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_pen'] = function(block) {
  // Generate JavaScript for pen up/down.
  return 'Turtle.' + block.getFieldValue('PEN') +
      '(\'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_width'] = function(block) {
  // Generate JavaScript for setting the width.
  var width = Blockly.JavaScript.valueToCode(block, 'WIDTH',
      Blockly.JavaScript.ORDER_NONE) || '1';
  return 'Turtle.penWidth(' + width + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_visibility'] = function(block) {
  // Generate JavaScript for changing turtle visibility.
  return 'Turtle.' + block.getFieldValue('VISIBILITY') +
      '(\'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_print'] = function(block) {
  // Generate JavaScript for printing text.
  var argument0 = String(Blockly.JavaScript.valueToCode(block, 'TEXT',
      Blockly.JavaScript.ORDER_NONE) || '\'\'');
  return 'Turtle.drawPrint(' + argument0 + ', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_font'] = function(block) {
  // Generate JavaScript for setting the font.
  return 'Turtle.drawFont(\'' + block.getFieldValue('FONT') + '\',' +
      Number(block.getFieldValue('FONTSIZE')) + ',\'' +
      block.getFieldValue('FONTSTYLE') + '\', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_move'] = function(block) {
  // Generate JavaScript for moving forward or backwards.
  var value = Blockly.JavaScript.valueToCode(block, 'VALUE',
      Blockly.JavaScript.ORDER_NONE) || '0';
  return 'Turtle.' + block.getFieldValue('DIR') +
      '(' + value + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_turn'] = function(block) {
  // Generate JavaScript for turning left or right.
  var value = Blockly.JavaScript.valueToCode(block, 'VALUE',
      Blockly.JavaScript.ORDER_NONE) || '0';
  return 'Turtle.' + block.getFieldValue('DIR') +
      '(' + value + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_width'] = function(block) {
  // Generate JavaScript for setting the width.
  var width = Blockly.JavaScript.valueToCode(block, 'WIDTH',
      Blockly.JavaScript.ORDER_NONE) || '1';
  return 'Turtle.penWidth(' + width + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_colour'] = function(block) {
  // Generate JavaScript for setting the colour.
  var colour = Blockly.JavaScript.valueToCode(block, 'COLOUR',
      Blockly.JavaScript.ORDER_NONE) || '\'#000000\'';
     Blockly.JavaScript.definitions_["%1"] = "var hello var hi;"
     Blockly.JavaScript.definitions_["%2"] = "iiiiiiiiiiiiiii"
  return 'Turtle.penColour(' + colour + ', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_repeat_internal'] = Blockly.JavaScript['controls_repeat'];

Blockly.JavaScript['turtle_setup_loop'] = Blockly.JavaScript['setup_loop'];

/**
 * The generated code for turtle blocks includes block ID strings.  These are useful for
 * highlighting the currently running block, but that behaviour is not supported in Android Blockly
 * as of May 2016.  This snippet generates the block code normally, then strips out the block IDs
 * for readability when displaying the code to the user.
 *
 * Post-processing the block code in this way allows us to use the same generators for the Android
 * and web versions of the turtle.
 */
Blockly.JavaScript.workspaceToCodeWithId = Blockly.JavaScript.workspaceToCode;

Blockly.JavaScript.workspaceToCode = function(workspace) {
  var code = this.workspaceToCodeWithId(workspace);
  // Strip out block IDs for readability.
  code = goog.string.trimRight(code.replace(/(,\s*)?'block_id_[^']+'\)/g, ')'))
  return code;
};