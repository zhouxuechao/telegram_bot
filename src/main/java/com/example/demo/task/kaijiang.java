package com.example.demo.task;

import com.example.demo.config.CutPicture;
import com.example.demo.config.MySqlConnection;
import com.example.demo.domain.TbXiazhujilu;
import com.example.demo.init.ExampleBotWithSession;
import com.example.demo.service.ITbFromService;
import com.example.demo.service.ITxiazhulishiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class kaijiang {

    @Autowired
    private ITxiazhulishiService iTxiazhulishiService;

    @Autowired
    private ITbFromService iTbFromService;

    final static String CHAT_ID = "-1001935906370";


    @Autowired
    private ExampleBotWithSession exampleBotWithSession;


    @Scheduled(cron = "30 11,31,51 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ?")
    public void kaijiang() throws Exception {
        //获取开奖号码推送
        String path = CutPicture.jietuguangwang();
        if(null != path && path.startsWith("/home")){
            System.out.println("截图官网开始推送");
            exampleBotWithSession.sendPhoto(new File(path),CHAT_ID,false);
            System.out.println("截图官网推送完成");
            deleteFile(path);
        }else{
            System.out.println("手动开奖开始推送");
            exampleBotWithSession.sendMsg(path,Long.valueOf(CHAT_ID),false);
            System.out.println("手动开奖推送完成");
        }
        //获取下注记录结算用户并推送开始结算
        File file = new File("/home/3.jpg");
        exampleBotWithSession.sendPhoto(file,CHAT_ID,false);

        new Thread(() -> {
                try {
                    String[] number = MySqlConnection.chaxunkaijianghaoma();
                    List<TbXiazhujilu> list = iTxiazhulishiService.selectAllList();
                    for (TbXiazhujilu t:list) {
                        if(t.getType() == 1){
                            if(Arrays.asList( number[0].split(",")).contains(t.getNumber()) && number[1].equals(t.getQihao())){
                                jiesuan(t.getFromId(),t.getType(),t.getPrice(),t.getOrderId());
                                continue;
                            }
                        }else if(t.getType() == 2 || t.getType() == 3 || t.getType() == 4 || t.getType() == 5){
                            if(Arrays.asList(t.getNumber().split(",")).stream().allMatch(e-> Arrays.asList(number[0].split(",")).contains(e)
                                    && number[1].equals(t.getQihao()))){
                                jiesuan(t.getFromId(),t.getType(),t.getPrice(),t.getOrderId());
                                continue;
                            }
                        }else if(t.getType() == 6 || t.getType() == 7 || t.getType() == 8 ){
                            if(Arrays.asList(number[0].split(",")).stream().allMatch(e-> Arrays.asList(t.getNumber().split(",")).contains(e)
                                    && number[1].equals(t.getQihao()))){
                                jiesuan(t.getFromId(),t.getType(),t.getPrice(),t.getOrderId());
                                continue;
                            }
                        }
                        iTxiazhulishiService.updateByPrimaryKeySelective(1,t.getOrderId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("结算失败");
                }
        }).start();

    }

    @Scheduled(cron = "0 9,28,48 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ?")
    public void tingzhixiazhu() throws IOException {
        //推送停止下注图片
        File file = new File("/home/1.jpg");
        exampleBotWithSession.sendPhoto(file,CHAT_ID,false);
    }

    @Scheduled(cron = "0 12,32,52 9,10,11,12,13,14,15,16,17,18,19,20,21,22 * * ?")
    public void tuisongjihua() throws IOException {
        //推送开始下注
        File file = new File("/home/2.jpg");
        exampleBotWithSession.sendPhoto(file,CHAT_ID,false);
        //推送计划
        String path = CutPicture.jietujihua();
        if(null != path && path.startsWith("/home")){
            System.out.println("截图计划开始推送");
            exampleBotWithSession.sendPhoto(new File(path),CHAT_ID,false);
            System.out.println("截图计划推送完成");
            deleteFile(path);
        }else{
            System.out.println("手动计划开始推送");
            exampleBotWithSession.sendMsg(path,Long.valueOf(CHAT_ID),false);
            System.out.println("手动计划推送完成");
        }
    }


    @Scheduled(cron = "0 0/15 * * * ?")
    public void tuisongguize() throws IOException {
        //推送停止下注图片
        String text = "【广东11选5】\n充值提现查询记录请私聊机器人\n\n指令如下：\n充值+金额" +
                "\n例: /充值1000\n\n提现+金额+/+收款u地址\n例: /提现500/收款u地址xxxxxxx\n\n/查询余额\n\n/下注记录\n\n/充值记录\n\n/提现记录";
        exampleBotWithSession.sendMsg(text,Long.valueOf(CHAT_ID),false);
        File file = new File("/home/5.jpg");
        exampleBotWithSession.sendPhoto(file,CHAT_ID,true);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    private void jiesuan(Long fromId ,Integer type, BigDecimal price,String orderId){
        BigDecimal money = new BigDecimal("0.00");
        if(type == 1){
            money = price.multiply(new BigDecimal("2"));
        }else if(type == 2){
            money = price.multiply(new BigDecimal("4"));
        }else if(type == 3){
            money = price.multiply(new BigDecimal("10").subtract(price.divide(new BigDecimal("2"))));
        }else if(type == 4){
            money = price.multiply(new BigDecimal("40").subtract(price));
        }else if(type == 5){
            money = price.multiply(new BigDecimal("100").subtract(price.multiply(new BigDecimal("3"))));
        }else if(type == 6){
            money = price.multiply(new BigDecimal("45"));
        }else if(type == 7){
            money = price.multiply(new BigDecimal("13"));
        }else if(type == 8){
            money = price.multiply(new BigDecimal("5").subtract(price.divide(new BigDecimal("2"))));
        }
        iTbFromService.updatejiaqian(money,fromId);
        iTxiazhulishiService.updateByPrimaryKeySelective(0,orderId);
    }
}
