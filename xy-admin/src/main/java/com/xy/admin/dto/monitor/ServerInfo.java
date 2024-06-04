package com.xy.admin.dto.monitor;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import com.xy.common.constant.Constants;
import lombok.Data;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 服务器相关信息DTO
 */
@Data
public class ServerInfo {

    // OSHI等待时间
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息DTO
     */
    private CpuInfo cpuInfo = new CpuInfo();

    /**
     * 内存相关信息DTO
     */
    private MemoryInfo memoryInfo = new MemoryInfo();

    /**
     * JVM相关信息DTO
     */
    private JvmInfo jvmInfo = new JvmInfo();

    /**
     * 服务器相关信息DTO
     */
    private SystemInfo systemInfo = new SystemInfo();

    /**
     * 磁盘相关信息列表
     */
    private List<DiskInfo> diskInfos = new LinkedList<>();

    /**
     * 填充ServerInfo对象的静态方法
     *
     * @return 填充后的ServerInfo对象
     */
    public static ServerInfo fillInfo() {
        ServerInfo serverInfo = new ServerInfo();

        // 获取系统信息和硬件抽象层
        oshi.SystemInfo si = new oshi.SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        // 填充CPU信息
        serverInfo.fillCpuInfo(hal.getProcessor());
        // 填充内存信息
        serverInfo.fillMemoryInfo(hal.getMemory());
        // 填充服务器信息
        serverInfo.fillSystemInfo();
        // 填充JVM信息
        serverInfo.fillJvmInfo();
        // 填充磁盘信息
        serverInfo.fillDiskInfos(si.getOperatingSystem());

        return serverInfo;
    }

    /**
     * 填充CPU信息
     *
     * @param processor CentralProcessor对象
     */
    private void fillCpuInfo(CentralProcessor processor) {
        // 获取CPU负载信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softIrq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long ioWait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;

        // 设置CPU信息
        cpuInfo.setCpuNum(processor.getLogicalProcessorCount());
        cpuInfo.setTotal(totalCpu);
        cpuInfo.setSys(cSys);
        cpuInfo.setUsed(user);
        cpuInfo.setWait(ioWait);
        cpuInfo.setFree(idle);
    }

    /**
     * 填充内存信息
     *
     * @param memory GlobalMemory对象
     */
    private void fillMemoryInfo(GlobalMemory memory) {
        // 设置内存信息
        memoryInfo.setTotal(memory.getTotal());
        memoryInfo.setUsed(memory.getTotal() - memory.getAvailable());
        memoryInfo.setFree(memory.getAvailable());
    }

    /**
     * 填充服务器信息
     */
    private void fillSystemInfo() {
        // 获取系统属性
        Properties props = System.getProperties();

        // 设置服务器信息
        systemInfo.setComputerName(NetUtil.getLocalHostName());
        systemInfo.setComputerIp(NetUtil.getLocalhost().getHostAddress());
        systemInfo.setOsName(props.getProperty("os.name"));
        systemInfo.setOsArch(props.getProperty("os.arch"));
        systemInfo.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 填充JVM信息
     */
    private void fillJvmInfo() {
        // 获取系统属性
        Properties props = System.getProperties();

        // 设置JVM信息
        jvmInfo.setTotal(Runtime.getRuntime().totalMemory());
        jvmInfo.setMax(Runtime.getRuntime().maxMemory());
        jvmInfo.setFree(Runtime.getRuntime().freeMemory());
        jvmInfo.setVersion(props.getProperty("java.version"));
        jvmInfo.setHome(props.getProperty("java.home"));
    }

    /**
     * 填充磁盘信息
     *
     * @param os OperatingSystem对象
     */
    private void fillDiskInfos(OperatingSystem os) {
        // 获取文件系统信息
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            // 计算磁盘使用情况
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setDirName(fs.getMount());
            diskInfo.setSysTypeName(fs.getType());
            diskInfo.setTypeName(fs.getName());
            diskInfo.setTotal(convertFileSize(total));
            diskInfo.setFree(convertFileSize(free));
            diskInfo.setUsed(convertFileSize(used));
            if (total != 0){
                diskInfo.setUsage(NumberUtil.div(used * 100, total, 4));
            } else {
                // Windows下如果有光驱（可能是虚拟光驱），total为0，不能作为除数
                diskInfo.setUsage(0);
            }
            diskInfos.add(diskInfo);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后的字符串
     */
    public String convertFileSize(long size) {
        float castedSize = (float) size;

        if (size >= Constants.GB) {
            return String.format("%.1f GB", castedSize / Constants.GB);
        }

        if (size >= Constants.MB) {
            return String.format("%.1f MB", castedSize / Constants.MB);
        }

        return String.format("%.1f KB", castedSize / Constants.KB);
    }
}
