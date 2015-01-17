package com.zsy.tool.dto;

/**
 * @author zhoushaoyu
 * 
 */
public class TopInfoDto {
	private long pid;
	private int pr;
	private int cpu; // %
	private String s;
	private long thr;
	private long vss; // K
	private long rss; // K
	private String pcy;
	private String uid;
	private String name;

	public TopInfoDto(String pid, String pr, String cpu, String s, String thr,
			String vss, String rss, String pcy, String uid, String name) {
		super();
		this.pid = Long.parseLong(pid);
		this.pr = Integer.parseInt(pr);
		this.cpu = Integer.parseInt(cpu.replaceAll("%", "").trim());
		this.s = s;
		this.thr = Long.parseLong(thr);
		this.vss = Long.parseLong(vss.replaceAll("K", "").trim());
		this.rss = Long.parseLong(rss.replaceAll("K", "").trim());
		this.pcy = pcy;
		this.uid = uid;
		this.name = name;
	}

	public TopInfoDto(long pid, int pr, String cpu, String s, long thr,
			String vss, String rss, String pcy, String uid, String name) {
		super();
		this.pid = pid;
		this.pr = pr;
		this.cpu = Integer.parseInt(cpu.replaceAll("%", "").trim());
		this.s = s;
		this.thr = thr;
		this.vss = Long.parseLong(vss.replaceAll("K", "").trim());
		this.rss = Long.parseLong(rss.replaceAll("K", "").trim());
		this.pcy = pcy;
		this.uid = uid;
		this.name = name;
	}

	public TopInfoDto(long pid, int pr, int cpu, String s, long thr, long vss,
			long rss, String pcy, String uid, String name) {
		super();
		this.pid = pid;
		this.pr = pr;
		this.cpu = cpu;
		this.s = s;
		this.thr = thr;
		this.vss = vss;
		this.rss = rss;
		this.pcy = pcy;
		this.uid = uid;
		this.name = name;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getPr() {
		return pr;
	}

	public void setPr(int pr) {
		this.pr = pr;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public long getThr() {
		return thr;
	}

	public void setThr(long thr) {
		this.thr = thr;
	}

	public long getVss() {
		return vss;
	}

	public void setVss(long vss) {
		this.vss = vss;
	}

	public long getRss() {
		return rss;
	}

	public void setRss(long rss) {
		this.rss = rss;
	}

	public String getPcy() {
		return pcy;
	}

	public void setPcy(String pcy) {
		this.pcy = pcy;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getTitle()
	{
		return "进程号\tCPU\t线程数\t虚拟内存\t常驻内存\tfg/bg\n用户\t进程名";
	}
	
	@Override
	public String toString() {
		return pid + "\t" + cpu
				+ "\t" + thr + "\t" + vss + "\t" + rss
				+ "\t" + pcy + "\t" + uid + "\n" + name;
	}

}
