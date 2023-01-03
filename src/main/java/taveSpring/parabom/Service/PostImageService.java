package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.ImageRepository;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImageService {



//    @Value("${custom.path.uploadPath}") // application.yml의 itemImgLocation 값을 불러와서 itemImgLocation 변수에다 넣어줌
    private String imageLocation = "D:\\22-12-Parabom-Project-new";

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final FileService fileService;

    public void saveImage(Image image, MultipartFile imagefile) throws Exception{

        String oriFileName = imagefile.getOriginalFilename();
        String filename = "";
        String path = "";

        // 파일 업로드
        if(!oriFileName.isEmpty()){
            // 상품 이미지 이름 = 저장할 경로 + 파일이름 + 파일크기(byte)
            filename = fileService.uploadFile(imageLocation, oriFileName, imagefile.getBytes());
            // 저장한 상품 이미지를 불러올 경로
            path = "D:\\22-12-Parabom-Project-new/" + filename;
        }

        // 이미지 정보 저장
        image.updateImage(oriFileName, filename, path);
        imageRepository.save(image);
//        postRepository.save(image);
    }
}