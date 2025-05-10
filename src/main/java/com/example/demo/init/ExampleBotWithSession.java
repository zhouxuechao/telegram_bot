package com.example.demo.init;

import com.example.demo.config.MySqlConnection;
import com.example.demo.config.SpringUtils;
import com.example.demo.domain.TbChongzhi;
import com.example.demo.domain.TbFrom;
import com.example.demo.domain.TbXiazhujilu;
import com.example.demo.enums.GdEnum;
import com.example.demo.enums.MessageEnum;
import com.example.demo.service.ITbFromService;
import com.example.demo.service.ITbchongzhiService;
import com.example.demo.service.ITxiazhulishiService;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExampleBotWithSession extends TelegramLongPollingBot {

    private final static String MESSAGE_KT = "/";

    private ITbFromService iTbFromService = SpringUtils.getBean(ITbFromService.class);
    private ITbchongzhiService iTbchongzhiService = SpringUtils.getBean(ITbchongzhiService.class);
    private ITxiazhulishiService iTxiazhulishiService = SpringUtils.getBean(ITxiazhulishiService.class);

    //填你自己的token和username
    @Value("${tg.token:6368180811:AAGQ_VcnrAnMNnLIHH9cKWu5i3mb_a8kn70}")
    private String token = "6368180811:AAGQ_VcnrAnMNnLIHH9cKWu5i3mb_a8kn70";
    @Value("${tg.username:guangdongCpBot}")
    private String username = "guangdongCpBot";

//    private String token ="6404943149:AAH8GIzaiiIu_bKp_CpSq7jKPBVa6mEqYxo";
//    private String username ="testjhhbBot";
    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Transactional
    @Override
    public void onUpdateReceived(Update update) {
        if (null != update.getCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            Long fromId = update.getCallbackQuery().getFrom().getId();
            //判断是否有该用户 没有就新增
            if(null == iTbFromService.selectByPrimaryKey(fromId)){
                TbFrom from = new TbFrom();
                BeanUtils.copyProperties(message.getFrom(),from);
                iTbFromService.insertSelective(from);
            }
            Long chatId = message.getChatId();
            if(update.getCallbackQuery().getData().equals("查询余额")){
                chaxunjilu(chatId,fromId);
                return;
            }else if(update.getCallbackQuery().getData().equals("下注记录")){
                xiazhujilu(chatId,fromId,10);
                return;
            }else if(update.getCallbackQuery().getData().equals("充值记录")){
                chongzhijilu(chatId,fromId,10);
                return;
            }else if(update.getCallbackQuery().getData().equals("提现记录")){
                tixianjilu(chatId,fromId,10);
                return;
            }
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            Long fromId = message.getFrom().getId();
            //判断是否有该用户 没有就新增
            if(null == iTbFromService.selectByPrimaryKey(fromId)){
                TbFrom from = new TbFrom();
                BeanUtils.copyProperties(message.getFrom(),from);
                iTbFromService.insertSelective(from);
            }
            if(!text.startsWith(MESSAGE_KT)){
                return;
            }
            if(message.getChat().getType().equals(MessageEnum.PRIVATE.getName())){
                Integer flag = null;
                if(text.startsWith(MESSAGE_KT + GdEnum.BETTING.getName())){
                    if(!text.startsWith(MESSAGE_KT + GdEnum.BETTING_HISTORY.getName())){
                        sendMsg("不处理该类指令："+text+",请点击群置顶查看规则！",chatId,false);
                        return;
                    }
                    flag = GdEnum.BETTING_HISTORY.getValue();
                }
                if(text.startsWith(MESSAGE_KT + GdEnum.RECHARGE.getName())){
                    if(text.startsWith(MESSAGE_KT + GdEnum.CHONGZHI_HISTORY.getName())){
                        flag = GdEnum.CHONGZHI_HISTORY.getValue();
                    }else {
                        flag = GdEnum.RECHARGE.getValue();
                    }
                }
                if(text.startsWith(MESSAGE_KT + GdEnum.WITHDRAWAL.getName())){
                    if(text.startsWith(MESSAGE_KT + GdEnum.TIXIAN_HISTORY.getName())){
                        flag = GdEnum.TIXIAN_HISTORY.getValue();
                    }else{
                        flag = GdEnum.WITHDRAWAL.getValue();
                    }

                }
                if(text.startsWith(MESSAGE_KT + GdEnum.QUERY_ACCOUNT.getName())){
                    flag = GdEnum.QUERY_ACCOUNT.getValue();
                }

                if(flag == null){
                    sendMsg("不处理该类指令："+text+",请点击群置顶查看规则！",chatId,false);
                    return;
                }

                if(flag == GdEnum.RECHARGE.getValue()){
                    text = text.substring(3,text.length());
                    if(!isNumeric(text)){
                        sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                        return;
                    }
                    if(Integer.parseInt(text) < 20){
                        sendMsg("充值金额不能小于20，请点击群置顶查看指令规则！",chatId,false);
                        return;
                    }
                    Double u = Double.parseDouble(text) / 7;
                    TbChongzhi tbChongzhi = new TbChongzhi();
                    tbChongzhi.setOrderId(getUUID());
                    tbChongzhi.setFromId(fromId);
                    tbChongzhi.setStatus(0);
                    tbChongzhi.setType(0);
                    tbChongzhi.setPrice(new BigDecimal(text));
                    iTbchongzhiService.insertSelective(tbChongzhi);
                    BigDecimal num = new BigDecimal(u);
                    BigDecimal money = num.setScale(2, BigDecimal.ROUND_DOWN);
                    sendMsg("充值地址 USDT-TRC20：\n" + "TJyBMeu8ge8VKWPpGRoBbhAERUgGF13P2k" +"\n充值u数量："+money+"\n充值成功请等待1-5分钟，若有问题，请联系管理员！",chatId,false);
                    return;
                }else if(flag == GdEnum.WITHDRAWAL.getValue()){
                    String tixmoney = null;
                    String tixiandizhi = null;
                    try{
                        tixmoney = text.substring(3,text.lastIndexOf("/"));
                        tixiandizhi = text.substring(text.lastIndexOf("/")+1);
                    }catch (Exception e){
                        sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                        return;
                    }
                    if(!isNumeric(tixmoney)){
                        sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                        return;
                    }
                    if(Integer.parseInt(tixmoney) < 20){
                        sendMsg("提现金额不能小于20，请点击群置顶查看指令规则!",chatId,false);
                        return;
                    }
                    if(StringUtils.isEmpty(tixiandizhi)){
                        sendMsg("提现地址不能为空！请输入收款u地址，格式：/提现100/u地址",chatId,false);
                        return;
                    }
                    //检验余额是否充足
                    TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
                    if(tbFrom.getBalance().compareTo(new BigDecimal(tixmoney)) == -1){
                        sendMsg(tbFrom.getFirstName() + tbFrom.getLastName() +" 余额不足，请充值！",chatId,false);
                        return;
                    }else{
                        TbChongzhi tbChongzhi = new TbChongzhi();
                        tbChongzhi.setOrderId(getUUID());
                        tbChongzhi.setPrice(new BigDecimal(tixmoney));
                        tbChongzhi.setFromId(fromId);
                        tbChongzhi.setStatus(0);
                        tbChongzhi.setType(1);
                        tbChongzhi.setDizhi(tixiandizhi);
                        iTbchongzhiService.insertSelective(tbChongzhi);

                        iTbFromService.updatejianqian(new BigDecimal(tixmoney),fromId);
                        sendMsg("提现成功，到账地址为："+tixiandizhi+"\n预计5-15分钟到账！",chatId,false);
                        return;
                    }
                }else if(flag == GdEnum.QUERY_ACCOUNT.getValue()){
                    chaxunjilu(chatId, fromId);
                    return;
                }else if(flag == GdEnum.BETTING_HISTORY.getValue()){
                    xiazhujilu(chatId, fromId,null);
                    return;
                }else if(flag == GdEnum.CHONGZHI_HISTORY.getValue()){
                    chongzhijilu(chatId, fromId,null);
                    return;
                }else if(flag == GdEnum.TIXIAN_HISTORY.getValue()){
                    tixianjilu(chatId, fromId,null);
                    return;
                }
            }else if(message.getChat().getType().equals(MessageEnum.GROUP.getName()) && chatId == -1001935906370L){
                if(!text.startsWith(MESSAGE_KT + GdEnum.BETTING.getName())
                || text.startsWith(MESSAGE_KT + GdEnum.BETTING_HISTORY.getName())){
                    sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                    return;
                }
                if(!checkxiazhu()){
                    sendMsg("当前休盘中，请等待开盘！",chatId,false);
                    return;
                }
                TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
                String xiazhuNumber = null;
                String xiazhumoney = null;
                try{
                    xiazhuNumber = text.substring(3,text.lastIndexOf("/"));
                    xiazhumoney = text.substring(text.lastIndexOf("/")+1);
                }catch (Exception e){
                    sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                    return;
                }
                if(!isNumeric(xiazhumoney)){
                    sendMsg("不处理该类指令："+text+",请点击群置顶查看指令规则！",chatId,false);
                    return;
                }
                if(Integer.parseInt(xiazhumoney) <= 0){
                    sendMsg("下注金额为0，请点击群置顶查看指令规则！",chatId,false);
                    return;
                }
                if(tbFrom.getBalance().compareTo(new BigDecimal(xiazhumoney)) == -1){
                    sendMsg(tbFrom.getFirstName() + tbFrom.getLastName() +" 余额不足，请充值！\n当前可用余额为："+tbFrom.getBalance(),chatId,false);
                    return;
                }
                TbXiazhujilu from = new TbXiazhujilu();
                from.setFromId(fromId);
                from.setStatus(2);
                String[] numbers = null;
                boolean flag = false;
                try {
                    numbers = xiazhuNumber.split(",");
                    for (String s: numbers) {
                        if(!isNumeric(s)){
                            flag = true;
                        }
                        int  num = Integer.parseInt(s);
                        if(num > 11 || num <0){
                            flag = true;
                        }
                    }
                }catch (Exception e){
                    sendMsg(tbFrom.getFirstName() + tbFrom.getLastName() +"下注失败，下注指令为：/下注/1,4,6/100",chatId,false);
                    return;
                }
                if(flag){
                    sendMsg(tbFrom.getFirstName() + tbFrom.getLastName() +"下注失败，下注指令为：/下注/1,4,6/100",chatId,false);
                    return;
                }
                from.setType(numbers.length);
                from.setNumber(xiazhuNumber);
                from.setPrice(new BigDecimal(xiazhumoney));
                from.setMoney(tbFrom.getBalance());
                from.setOrderId(getUUID());
                from.setQihao(MySqlConnection.chaxundangqianqihao());
                iTxiazhulishiService.insertSelective(from);

                iTbFromService.updatejianqian(new BigDecimal(xiazhumoney),tbFrom.getId());
                sendMsg(tbFrom.getFirstName() + tbFrom.getLastName() +"下注成功！",chatId,false);
                return;
            }
        }
    }

    private void tixianjilu(Long chatId, Long fromId,Integer limit) {
        TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
        List<TbChongzhi> tbChongzhis = iTbchongzhiService.selectList(fromId,1,limit);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tbFrom.getFirstName() + tbFrom.getLastName() + " 提现记录\n");
        if(CollectionUtils.isEmpty(tbChongzhis)){
            stringBuffer.append("空空如也~");
        }
        for (TbChongzhi t:tbChongzhis) {
            stringBuffer.append("提现订单号："+ t.getOrderId() +"\n");
            String a = t.getStatus()== 0 ?"提现中\n":t.getStatus()== 1 ?"失败\n":"成功\n";
            stringBuffer.append("订单状态：" + a);
            stringBuffer.append("订单金额：" + t.getPrice() +"\n");
            stringBuffer.append("提现u地址：" + t.getDizhi() +"\n");
            stringBuffer.append("创建时间：" + t.getCreateTime() +"\n");
            stringBuffer.append("到账时间：" + t.getUpdateTime() +"\n\n");
        }
        sendMsg( stringBuffer.toString(), chatId,false);
    }

    private void chongzhijilu(Long chatId, Long fromId,Integer limit) {
        TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
        List<TbChongzhi> tbChongzhis = iTbchongzhiService.selectList(fromId,0,limit);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tbFrom.getFirstName() + tbFrom.getLastName() + " 充值记录\n");
        if(CollectionUtils.isEmpty(tbChongzhis)){
            stringBuffer.append("空空如也~");
        }
        for (TbChongzhi t:tbChongzhis) {
            stringBuffer.append("充值订单号："+ t.getOrderId() +"\n");
            String a = t.getStatus()== 0 ?"充值中\n":t.getStatus()== 1 ?"失败\n":"成功\n";
            stringBuffer.append("订单状态：" + a);
            stringBuffer.append("充值金额：" + t.getPrice() +"\n");
            stringBuffer.append("创建时间：" + t.getCreateTime() +"\n");
            stringBuffer.append("到账时间：" + t.getUpdateTime() +"\n\n");
        }
        sendMsg( stringBuffer.toString(), chatId,false);
    }

    private void xiazhujilu(Long chatId, Long fromId,Integer limit) {
        TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
        List<TbXiazhujilu> tbXiazhujilus = iTxiazhulishiService.selectList(fromId,limit);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tbFrom.getFirstName() + tbFrom.getLastName() + " 下注记录\n");
        if(CollectionUtils.isEmpty(tbXiazhujilus)){
            stringBuffer.append("空空如也~");
        }
        for (TbXiazhujilu t:tbXiazhujilus) {
            stringBuffer.append("下注订单："+ t.getOrderId() +"\n");
            stringBuffer.append("下注号码：" + t.getNumber() + "\n");
            stringBuffer.append("下注金额：" + t.getPrice() + "\n");
            stringBuffer.append("下注类型：" + t.getType() +"中" + t.getType()+"\n");
            String a = t.getStatus() == 0 ?"是":t.getStatus() == 1?"否":"待开";
            stringBuffer.append("是否中奖：" + a +"\n");
            stringBuffer.append("下注时间：" + t.getCreateTime() +"\n");
            stringBuffer.append("结算时间：" + t.getUpdateTime() +"\n\n");
        }
        sendMsg( stringBuffer.toString(), chatId,false);
    }

    private void chaxunjilu(Long chatId, Long fromId) {
        TbFrom tbFrom = iTbFromService.selectByPrimaryKey(fromId);
        if(ObjectUtils.isEmpty(tbFrom)){

        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tbFrom.getFirstName() + tbFrom.getLastName() + " 查询余额\n");
        stringBuffer.append("用户名："+ tbFrom.getUserName() +"\n");
        stringBuffer.append("用户余额：" + tbFrom.getBalance() +"元");
        sendMsg( stringBuffer.toString(), chatId,false);
    }


    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    //回复普通文本消息
    public void sendMsg(String text,Long chatId,boolean falg){
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText(text);
        if(falg){
            response.setReplyMarkup(setKeyboardMarkup());
        }
        try {
            execute(response);
        } catch (TelegramApiException e) {
        }
    }

    public static InlineKeyboardMarkup setKeyboardMarkup(){
        InlineKeyboardButton button1 = InlineKeyboardButton.builder().text("充值").url("https://t.me/guangdongCpBot").build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder().text("提现").url("https://t.me/guangdongCpBot").build();
        InlineKeyboardButton button3 = InlineKeyboardButton.builder().text("查询余额").callbackData("查询余额").build();
        InlineKeyboardButton button4 = InlineKeyboardButton.builder().text("下注记录").callbackData("下注记录").build();
        InlineKeyboardButton button5 = InlineKeyboardButton.builder().text("充值记录").callbackData("充值记录").build();
        InlineKeyboardButton button6 = InlineKeyboardButton.builder().text("提现记录").callbackData("提现记录").build();
        InlineKeyboardButton button7 = InlineKeyboardButton.builder().text("联系管理员").url("https://t.me/intheend2").build();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        List<InlineKeyboardButton> list3 = new ArrayList<>();
        List<InlineKeyboardButton> list4 = new ArrayList<>();
        List<InlineKeyboardButton> list5 = new ArrayList<>();
        List<InlineKeyboardButton> list6 = new ArrayList<>();
        list1.add(button1);
        list1.add(button2);
        list2.add(button3);
        list3.add(button4);
        list4.add(button5);
        list5.add(button6);
        list6.add(button7);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Collections.addAll(rowList, list1, list2, list3, list4,list5,list6);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(rowList).build();
        return inlineKeyboardMarkup;
    }

    //发送图片
    public void sendPhoto(File text, String chatId,boolean falg){
        SendPhoto response = new SendPhoto();
        response.setChatId(String.valueOf(chatId));
        response.setPhoto(new InputFile(text));
        if(falg){
            response.setReplyMarkup(setKeyboardMarkup());
        }
        try {
            execute(response);
        } catch (TelegramApiException e) {
        }
    }

    @PostConstruct
    private void init(){;
        //梯子在自己电脑上就写127.0.0.1  软路由就写路由器的地址
        String proxyHost = "127.0.0.1";
        //端口根据实际情况填写，说明在上面，自己看
        int proxyPort = 1080;

        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);
        //注意一下这里，ProxyType是个枚举，看源码你就知道有NO_PROXY,HTTP,SOCKS4,SOCKS5;
        botOptions.setProxyType(DefaultBotOptions.ProxyType.NO_PROXY);

        DefaultBotSession defaultBotSession = new DefaultBotSession();
        defaultBotSession.setOptions(botOptions);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(defaultBotSession.getClass());


            //需要代理
//            ExecBot bot = new ExecBot(botOptions);
//            telegramBotsApi.registerBot(bot);
            //不需代理
            ExampleBotWithSession bot2 = new ExampleBotWithSession();
            telegramBotsApi.registerBot(bot2);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String getUUID() {
        String order = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
        return order + extracted();
    }

    private static int extracted() {
        int min = 10000000;
        int max = 99999999;
        Random random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert random != null;
        int a = random.nextInt(max) % (max - min + 1) + min;
        return a;
    }


    public static boolean checkxiazhu(){
        // 创建时间对象
        Calendar time1 = Calendar.getInstance();
        // 获取时、分、秒
        int hour1 = time1.get(Calendar.HOUR_OF_DAY);
        int minute1 = time1.get(Calendar.MINUTE);
        int second1 = time1.get(Calendar.SECOND);
        if(hour1 < 9 || (hour1 == 23 && hour1 > 11)){
            return false;
        }
        if((minute1 ==12 && second1 >= 10)
        || (minute1 == 12 && second1 >= 10)
        || (minute1 == 32 && second1 >= 10)
        || minute1 == 52 && second1 >= 10){
            return true;
        }
        if((minute1 >12 && minute1 < 28)
        || (minute1 > 32 && minute1 < 48)
        || (minute1 > 52 || minute1 < 9)){
            return true;
        }
        return false;
    }
}
