package taveSpring.parabom.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import taveSpring.parabom.Domain.Image;

public class ImageDto {

    private static ModelMapper modelMapper = new ModelMapper();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfoDto {
        private Long id;
        private String path;
        private String fileName;
        private String oriFileName;

        public static ImageDto.ImageInfoDto of(Image image) {
            return modelMapper.map(image, ImageDto.ImageInfoDto.class);
            // entity -> dto
        }

        public Image toEntity() {
            return Image.builder().id(id).path(path).fileName(fileName).oriFileName(oriFileName).build();
        }



    }
}
