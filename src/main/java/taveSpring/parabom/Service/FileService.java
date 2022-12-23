package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {

    public String uploadFile(String uploadPath, String oriFileName, byte[] fileData) throws Exception{

        UUID uuid = UUID.randomUUID(); // 중복 방지용 이름 생성
        String extension = oriFileName.substring(oriFileName.lastIndexOf(".")); // 가장 뒤에 있는 .부터 잘라 리턴

        // UUID 로 받은 값 + 원래 파일 확장자 = savedFileName: 저장될 파일 이름
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // FileOutputStream: 파라미터의 파일을 쓰기 위한 객체를 생성. 바이트 단위로 데이터를 읽음.
        // 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨서, 파일에 쓸 파일 출력 스트림 생성
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData); // 파일의 byte를 읽어와서 파일 스트림에 입력
        fos.close();
        return savedFileName; // 파일 이름만 반환
    }

    public void deleteFile(String filePath) throws Exception{

        File deleteFile = new File(filePath); // 파일 저장 경로를 이용해서 파일 객체 생성

        if(deleteFile.exists()) {
            deleteFile.delete();
        } else {
        }
    }
}
