package com.yzl.springbootsecurity.controller;

import com.yzl.springbootsecurity.config.constants.MyConstants;
import com.yzl.springbootsecurity.vo.ImageCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2020-07-02 16:26
 */
@RestController
@RequestMapping("/img-code")
public class ImgCodeLoginController
{
    @Autowired
    private SessionStrategy sessionStrategy;


    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "账号"),
            @ApiImplicitParam(name = "password",value = "密码"),
            @ApiImplicitParam(name = "code",value = "验证码")
    }
    )
    public void login(String account ,String password,String code){
    }

    @GetMapping("/getCode")
    @ApiOperation(value = "获取验证码")
    public void getCode(HttpServletResponse response, HttpServletRequest request){



        int width = 70;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        //System.out.print(hash1);
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String capstr = hash1.substring(0, 4);
        //HttpSession session = request.getSession();
       // session.setAttribute("code", capstr);
       // removeAttrbute(session,"code",1);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(capstr, 8, 24);
        g.dispose();
        response.setContentType("image/jpeg");
        ImageCode imageCode = new ImageCode(image,capstr, 5*60);
        sessionStrategy.setAttribute(new ServletWebRequest(request), MyConstants.SESSION_KEY, imageCode);
        OutputStream strm;
        try {
            strm = response.getOutputStream();
            ImageIO.write(image, "jpeg", strm);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void removeAttrbute(final HttpSession session, final String attrName,final Integer minute) {

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                // 删除session中存的验证码
                System.out.println("删除session中存的验证码");
                if (Objects.nonNull(session.getAttribute(attrName))){
                    session.removeAttribute(attrName);
                }
            }
        }, minute , TimeUnit.MINUTES);
    }



    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = generate(new ServletWebRequest(request));
        sessionStrategy.setAttribute(new ServletWebRequest(request), MyConstants.SESSION_KEY, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }


    /**
     * 生成图形验证码
     * @param request
     * @return
     */
    private ImageCode generate(ServletWebRequest request) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",MyConstants.WIDTH);
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", MyConstants.HEIGHT);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        int length = ServletRequestUtils.getIntParameter(request.getRequest(), "length", MyConstants.RANDOM_SIZE);
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, sRand, MyConstants.EXPIRE_SECOND);
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
