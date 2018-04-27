package com.zhong.pms.config.datasource;

import com.gome.datasource.AESCodec;

/**
 * @author zhangliewei
 * @date 2018/3/21 16:37
 * @opyright(c) gome inc Gome Co.,LTD
 */
public class GomeDruidDataSource extends com.gome.datasource.GomeDruidDataSource {

    /*** 数据源名称*/
    private String name;

    /***数据源是否是master,只能有一个master**/
    private Boolean master;

    /*** 权重*/
    private Integer weight = 1;

    /**
     * slave 数据检查数据源是否可用时间间隔
     **/
    private Long interval;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public static void main(String[] args) {
        System.out.println(" username : " + AESCodec.Encrypt("root", "0132457689BACDFE"));
        String s=AESCodec.Encrypt("root", "0132457689BACDFE");
        System.out.println(AESCodec.Decrypt(s,"0132457689BACDFE"));
        System.out.println(" username : " + AESCodec.Encrypt("axwl_pt@201803", "0132457689BACDFE"));
//		System.out.println(AESCodec.Decrypt("f700094d094fee13e0afb7ba9200e3ed0fc768ad6a08882ae5551f74704dcb5a650576dc5fac95e0cd2e33a480386bed363fdc9efdee5a6d41cb5e209be88e2aa71f0815391d868236ae203223a1f0561a6baa16f07f2f690e2cea1ec288bcf7",DEFAULT_CODEC_KEY ));
//		System.out.println(" password : " + AESCodec.Encrypt("mars@yb", DEFAULT_CODEC_KEY));
//		System.out.println(" url : " + AESCodec.Encrypt("jdbc:mysql://10.128.45.16:3306/ehm-qe?useUnicode=true&amp;characterEncoding=UTF-8", DEFAULT_CODEC_KEY));
//		System.out.println(" url : " + AESCodec.Encrypt("jdbc:mysql://10.128.45.17:3306/ehm-qe?useUnicode=true&amp;characterEncoding=UTF-8", DEFAULT_CODEC_KEY));
//		System.out.println(" url : " + AESCodec.Encrypt("jdbc:mysql://10.128.45.18:3306/ehm-qe?useUnicode=true&amp;characterEncoding=UTF-8", DEFAULT_CODEC_KEY));
//		System.out.println(" url : " + AESCodec.Encrypt("jdbc:mysql://10.128.45.19:3306/ehm-qe?useUnicode=true&amp;characterEncoding=UTF-8", DEFAULT_CODEC_KEY));
    }
}
