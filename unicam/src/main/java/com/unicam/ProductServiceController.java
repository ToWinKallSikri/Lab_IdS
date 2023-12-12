package com.unicam;

import com.unicam.Exception.ProductNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class ProductServiceController {

//    private static Map<String, Product> productRepo = new HashMap<>();
   private ProductListRepository productRepository;
//
//    static {
//        Product miele = new Product();
//        miele.setId("1");
//        miele.setName("Miele");
//        productRepo.put(miele.getId(), miele);
//
//        Product zucchero = new Product();
//        zucchero.setId("2");
//        zucchero.setName("Zucchero");
//        productRepo.put(zucchero.getId(), zucchero);
//    }

    @Autowired //in automatico il sistema avvia il costruttore, quando viene avviato il programma
    public ProductServiceController(ProductListRepository productRepository) {
        this.productRepository = productRepository;

        Product miele = new Product();
        miele.setId("1");
        miele.setName("Miele");
        //productRepo.put(miele.getId(), miele);
        productRepository.save(miele);

        Product zucchero = new Product();
        zucchero.setId("2");
        zucchero.setName("Zucchero");
        //productRepo.put(zucchero.getId(), zucchero);
        productRepository.save(zucchero);
    }

//    @RequestMapping(value = "/products")
//    public ResponseEntity<Object> getProducts(){
//        return new ResponseEntity<Object>(productRepo.values(), HttpStatus.OK);
//    }

    @RequestMapping(value = "/products")
    public ResponseEntity<Object> getProducts(){
        return new ResponseEntity<Object>(productRepository.findAll(), HttpStatus.OK);
    }

//    @GetMapping(value="/product/{id}")
//    public ResponseEntity<Object> getProduct(@PathVariable("id") String id){
//        return new ResponseEntity<Object>(productRepo.get(id), HttpStatus.OK);
//    }

    @GetMapping(value = "/product")
    public ResponseEntity<Object> getProduct(@PathParam("id") String id){
        System.out.println("id: " + id);
        return new ResponseEntity<Object>(productRepository.findById(id), HttpStatus.OK);
    }

//    @GetMapping("/product")
//    public ResponseEntity<Object> getProductNew(@PathParam("id") String id) {
//        return new ResponseEntity<Object>(productRepo.get(id), HttpStatus.OK);
//    }

//    @PostMapping("/product")
//    public ResponseEntity<Object> addProduct(@RequestBody Product product) {
//        if (!productRepo.containsKey(product.getId())) {
//            productRepo.put(product.getId(),product);
//            return new ResponseEntity<>("Prodotto inserito",HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Prodotto già esistente", HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/product")
    public ResponseEntity<Object> addProduct(@RequestBody Product product) {
        if (!productRepository.existsById(product.getId())) {
            productRepository.save(product);
            return new ResponseEntity<>("Prodotto inserito",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Prodotto già esistente", HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping("/product/{id}")
//    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String id) {
//        if (productRepo.containsKey(id)) {
//            productRepo.remove(id);
//            return new ResponseEntity<>("Prodotto eliminato",HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Prodotto non esistente",HttpStatus.BAD_REQUEST);
//        }
//    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Prodotto eliminato",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Prodotto non esistente",HttpStatus.BAD_REQUEST);
        }
    }

//    @RequestMapping(value = "/product", method=RequestMethod.PUT)
//    public ResponseEntity<Object> updateProduct (@PathParam("id") String id,@RequestBody Product product) {
//        if (productRepo.containsKey(id)) {
//            productRepo.get(id).setName(product.getName());
//            return new ResponseEntity<>("Prodotto modificato",HttpStatus.OK);
//        } else {
//           throw new ProductNotFoundException();
//        }
//    }
    @RequestMapping(value = "/product", method=RequestMethod.PUT)
    public ResponseEntity<Object> updateProduct (@PathParam("id") String id,@RequestBody Product product) {
        if ((productRepository.existsById(id))) {
            productRepository.deleteById(id);
            product.setId(id);
            productRepository.save(product);
            return new ResponseEntity<>("Prodotto modificato",HttpStatus.OK);
        } else {
            throw new ProductNotFoundException();
        }
    }

    //utente da remoto invia file, in una cartella del file system del server
    @RequestMapping(value = "/fileUpload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam("filename") String filename) throws IOException {
        File newFile = new File("C:/Users/Twinkal/OneDrive/Desktop/" + file.getOriginalFilename());
        newFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(newFile);
        fileOut.write(file.getBytes());
        fileOut.close();
        return new ResponseEntity<>("File saved",HttpStatus.OK);
    }

    @GetMapping(value="/download")
    public ResponseEntity<Object> fileDownload(@PathParam("filename") String filename) throws FileNotFoundException{
        String fileN = "/Users/Twinkal/OneDrive/Desktop/" + filename;
        File file = new File(fileN);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", String.format("attachment; filename =\"%s\"", file.getName()));
        header.add("Cache-control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ResponseEntity response = ResponseEntity.ok().headers(header).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/txt")).body(resource);
        return response;
    }
}
