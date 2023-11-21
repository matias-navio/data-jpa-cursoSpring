package com.bolsadeideas.springboot.app.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    @Override
    public Resource load(String filename) throws MalformedURLException{

        Path pathFoto = getPath(filename);
        log.info("pathFoto: "+pathFoto);
        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if(!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("Error: no se puede recargar la imagen: " + pathFoto.toString());
        }

        return recurso;
    }

    @Override
    public String copy(MultipartFile file) throws IOException{

        // crea un nombre random para las fotos que no se repita ni se sobreescriba
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // en esta ruta se van a guardad las fotos, dentro del proyecto
        Path rootPath = getPath(uniqueFileName);
        log.info("rootPath: " + rootPath);

        Files.copy(file.getInputStream(), rootPath);

        return uniqueFileName;
    }

    @Override
    public boolean delete(String filename) {

        Path rootPath = getPath(filename);
        File archivo = rootPath.toFile();

        if(archivo.exists() && archivo.canRead()){
            if(archivo.delete()){
                return true;
            }
        }
        return false;
    }

    public Path getPath(String filename){
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
