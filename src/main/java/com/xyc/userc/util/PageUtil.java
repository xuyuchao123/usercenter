package com.xyc.userc.util;

import com.xyc.userc.service.impl.ViolationServiceImpl;
import com.xyc.userc.vo.ViolationInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2021/4/3.
 */
public class PageUtil
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(PageUtil.class);

    public static <T> List getPageInfoList(List<T> list, String page, String size)
    {
        int cnt = list.size();
        int pageInt = Integer.valueOf(page);
        int sizeInt = Integer.valueOf(size);
        int pageCnt = cnt % sizeInt != 0 ? cnt / sizeInt + 1 : cnt / sizeInt;
        if(pageInt > pageCnt)
        {
            pageInt = pageCnt;
        }
        if(pageInt == 0)
        {
            pageInt = 1;
        }
        int start = (pageInt - 1) * sizeInt + 1;
        int end = start + sizeInt - 1;
        LOGGER.info("start={},end={},cnt={}",start,end,cnt);

        List<T> list_page = new ArrayList<>();
        for(int i = start-1; i <= end-1; i++)
        {
            if(i >= list.size())
            {
                break;
            }
            list_page.add(list.get(i));
        }
        List resList = new ArrayList();
        resList.add(cnt);
        resList.add(pageInt);
        resList.add(sizeInt);
        resList.add(list_page);
        return resList;
    }
}
