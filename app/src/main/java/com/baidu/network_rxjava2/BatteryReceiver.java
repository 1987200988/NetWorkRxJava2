package com.baidu.network_rxjava2;
  
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
  
public class BatteryReceiver extends BroadcastReceiver { 
  
    @Override
    public void onReceive(Context context, Intent intent) { 
        if (Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())) { 
            Toast.makeText(context, "电量已恢复，可以使用!", Toast.LENGTH_LONG).show(); 
        } 
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) { 
            Toast.makeText(context, "电量过低，请尽快充电！", Toast.LENGTH_LONG).show(); 
        }

        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) { 
            Bundle bundle = intent.getExtras(); 
            // 获取当前电量  
            int current = bundle.getInt("level"); 
            // 获取总电量  
            int total = bundle.getInt("scale"); 
            StringBuffer sb = new StringBuffer(); 
            sb.append("当前电量为：" + current * 100 / total + "%" + "  "); 
            // 如果当前电量小于总电量的15%  
            if (current * 1.0 / total < 0.15) { 
                sb.append("电量过低，请尽快充电！"); 
            } else { 
                sb.append("电量足够，请放心使用！"); 
            }

            int BatteryT = intent.getIntExtra("temperature", 0);
            sb.append("电池温度"+BatteryT);
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
            Log.e("abcd", "onReceive: "+sb.toString());

        } 
  
    } 
  
}
//intent.getIntExtra("level", 0);    ///电池剩余电量
//        intent.getIntExtra("scale", 0);  ///获取电池满电量数值
//        intent.getStringExtra("technology");  ///获取电池技术支持
//        intent.getIntExtra("status",BatteryManager.BATTERY_STATUS_UNKNOWN); ///获取电池状态
//        intent.getIntExtra("plugged", 0);  ///获取电源信息
//        intent.getIntExtra("health",BatteryManager.BATTERY_HEALTH_UNKNOWN);  ///获取电池健康度
//        intent.getIntExtra("voltage", 0);  ///获取电池电压
//        intent.getIntExtra("temperature", 0);  ///获取电池温度
//
//        作者：ShawnXiaFei
//        链接：https://www.jianshu.com/p/6c8286d451c8
//        來源：简书
//        简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。