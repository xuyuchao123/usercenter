package com.xyc.userc.util;

/**
 * Created by 1 on 2021/1/26.
 */
public  class DataUtil
{
    //判断输入字符串每一位是否为数字 0~9
    public static boolean isNumeric(String str)
    {
        for(int i = 0; i < str.length(); i++)
        {
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
            {
                return false;
            }
        }
        return true;
    }
}
