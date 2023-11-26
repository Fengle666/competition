package com.yjq.programmer.controller;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.util.CommonUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-04 13:52
 */
@RequestMapping("/file")
@RestController
public class FileController {

    @Value("${yjq.upload.path}")
    private String uploadFilePath;//文件保存位置

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 自定义上传文件处理
     * @param file
     * @return
     */
    @PostMapping(value="/upload_file")
    public ResponseDTO<String> uploadFile(MultipartFile file){
        if(file == null){
            return ResponseDTO.errorByMsg(CodeMsg.FILE_EMPTY);
        }
        //检查上传文件大小 不能超过100MB
        if(file.getSize() > 100*1024*1024) {
            return ResponseDTO.errorByMsg(CodeMsg.FILE_SURPASS_MAX_SIZE);
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String savePath = uploadFilePath + CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "\\";
        File savePathFile = new File(savePath);
        if(!savePathFile.exists()){
            //若不存在改目录，则创建目录
            savePathFile.mkdir();
        }
        String filename = new Date().getTime()+"."+suffix;
        logger.info("保存文件的路径:{}",savePath + filename);
        try {
            //将文件保存至指定目录
            file.transferTo(new File(savePath + filename));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.errorByMsg(CodeMsg.SAVE_FILE_EXCEPTION);
        }
        String filepath = CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + filename;
        return ResponseDTO.successByMsg(filepath, "文件上传成功！");
    }

    /**
     * 文件统一下载类
     * @param url
     * @param response
     * @return
     */
    @GetMapping(value="/download")
    public void downloadFile(HttpServletResponse response, String url, String filename){
        try {
            File file = new File(uploadFilePath, url);
            response.setHeader("Content-Disposition","attachment;filename=" + new String(filename.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            writeFile(response, file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印到浏览器上下载
     * @param response
     * @param file
     */
    public void writeFile(HttpServletResponse response, File file) {
        ServletOutputStream sos = null;
        FileInputStream aa = null;
        try {
            aa = new FileInputStream(file);
            sos = response.getOutputStream();
            // 读取文件问字节码
            byte[] data = new byte[(int) file.length()];
            IOUtils.readFully(aa, data);
            // 将文件流输出到浏览器
            IOUtils.write(data, sos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                sos.close();
                aa.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
