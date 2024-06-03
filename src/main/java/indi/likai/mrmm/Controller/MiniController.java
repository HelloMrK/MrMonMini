package indi.likai.mrmm.Controller;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@RequestMapping("/mini")
@RestController
public class MiniController {
    DecimalFormat df = new DecimalFormat("#.##");
    OperatingSystemMXBean operatingSystemMXBean= ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    /**
     * 懒得写注释了
     *
     *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
     * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
     * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
     * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
     * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
     *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
     *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
     *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
     *           ░     ░ ░      ░  ░

     * @param path :例:C:\ c C c:之类都可以
     * @return
     */
    @PostMapping("/getServerStatus")
    public String getServerStatus(String path){
        MiniInfo miniInfo=new MiniInfo(){

        };
        //使用HuTool获取内存
        BigDecimal totalMemorySize=new BigDecimal(OshiUtil.getMemory().getTotal() /1024.0/1024.0).setScale(2,2);
        BigDecimal usedMemorySize=new BigDecimal((OshiUtil.getMemory().getAvailable())/1024.0/1024.0).setScale(2,2);
        //使用HuTool获取Cpu
        //获取CPU使用信息
        CpuInfo cpuInfo=OshiUtil.getCpuInfo();
        BigDecimal usedCpuRate=new BigDecimal(cpuInfo.getUsed()).setScale(2,BigDecimal.ROUND_HALF_UP);
        //获取硬盘使用信息 根据传入的path来获取硬盘占用信息
        BigDecimal totalDiskSize = new BigDecimal("0.0");
        BigDecimal usedDiskSize = new BigDecimal("0.0");
        File[] roots = File.listRoots();
        for (File root:roots){
            if (dealPath(root.getPath()).equals(dealPath(path))){
                totalDiskSize=new BigDecimal(root.getTotalSpace()/1024.0/1024.0/1024.0).setScale(2,2);
                usedDiskSize=new BigDecimal((root.getTotalSpace()-root.getFreeSpace())/1024.0/1024.0/1024.0).setScale(2,2);
            }
        }
        //包装返回信息
        miniInfo.setUsedMemSize(usedMemorySize);
        miniInfo.setTotalMemSize(totalMemorySize);
        miniInfo.calcUsedMemRate();
        miniInfo.setUsedCpuRate(usedCpuRate);
        miniInfo.setTotalDiskSize(totalDiskSize);
        miniInfo.setUsedDiskSize(usedDiskSize);
        miniInfo.calcUsedDiskRate();
        return JSONObject.toJSONString(miniInfo);
    }

    /**
     * 处理path,将符号和斜线都去掉.只留盘符字母并转换成小写
     * @param path
     * @return
     */
    private String dealPath(String path){
        return path
                .replace(":\\","")
                .replace(":","")
                .replace("\\","")
                .toLowerCase();
    }
}
class MiniInfo{

    /** 总内存(MB) */
    private BigDecimal totalMemSize;
    /** 已使用内存(MB) */
    private BigDecimal usedMemSize;
    /** 已使用内存率(%) */
    private BigDecimal usedMemRate;
    /** 已使用CPU(%) */
    private BigDecimal usedCpuRate;

    /** 硬盘总空间(GB) */
    private BigDecimal totalDiskSize;

    /** 硬盘已用空间(GB) */
    private BigDecimal usedDiskSize;

    /** 硬盘已用空间(%) */
    private BigDecimal usedDiskRate;


    public BigDecimal getTotalMemSize() {

        return totalMemSize;
    }

    public void setTotalMemSize(BigDecimal totalMemSize) {
        this.totalMemSize = totalMemSize;
    }

    public BigDecimal getUsedMemSize() {
        return usedMemSize;
    }

    public void setUsedMemSize(BigDecimal usedMemSize) {
        this.usedMemSize = usedMemSize;
    }

    public BigDecimal getUsedMemRate() {
        return usedMemRate;
    }

    public void setUsedMemRate(BigDecimal usedMemRate) {
        this.usedMemRate = usedMemRate;
    }

    public BigDecimal getUsedCpuRate() {
        return usedCpuRate;
    }

    public void setUsedCpuRate(BigDecimal usedCpuRate) {
        this.usedCpuRate = usedCpuRate;
    }

    public BigDecimal getTotalDiskSize() {
        return totalDiskSize;
    }

    public void setTotalDiskSize(BigDecimal totalDiskSize) {
        this.totalDiskSize = totalDiskSize;
    }

    public BigDecimal getUsedDiskSize() {
        return usedDiskSize;
    }

    public void setUsedDiskSize(BigDecimal usedDiskSize) {
        this.usedDiskSize = usedDiskSize;
    }

    public BigDecimal getUsedDiskRate() {
        return usedDiskRate;
    }

    public void setUsedDiskRate(BigDecimal usedDiskRate) {
        this.usedDiskRate = usedDiskRate;
    }

    /**
     * 计算已使用内存率
     */
    public void calcUsedMemRate() {
        this.usedMemRate = (BigDecimal.valueOf(100).multiply(
                this.usedMemSize.divide(this.totalMemSize,2,BigDecimal.ROUND_HALF_DOWN)
        )
        ).setScale(2);
    }
    public void calcUsedDiskRate() {
        this.usedDiskRate = (BigDecimal.valueOf(100).multiply(
                this.usedDiskSize.divide(this.totalDiskSize,2,BigDecimal.ROUND_HALF_DOWN)
        )
        ).setScale(2);
    }
}
