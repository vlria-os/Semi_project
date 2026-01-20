package com.example.demo.controller;

import com.example.demo.dto.LocalProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApiAdsController {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final Map<String,String> REGION_AREA = Map.ofEntries(
            Map.entry("gwangju", "광주광역시"),
            Map.entry("jeonnam", "전라남도"),
            Map.entry("daejeon", "대전광역시"),
            Map.entry("busan", "부산광역시"),
            Map.entry("daegu", "대구광역시"),
            Map.entry("incheon", "인천광역시"),
            Map.entry("ulsan", "울산광역시"),
            Map.entry("jeonbuk", "전북특별자치도"),
            Map.entry("gyeonggi", "경기도"),
            Map.entry("chungbuk", "충청북도"),
            Map.entry("chungnam", "충청남도"),
            Map.entry("gyeongbuk", "경상북도"),
            Map.entry("gyeongnam", "경상남도"),
            Map.entry("gangwon", "강원특별자치도")
    );

    @GetMapping("/page")
    public String page(){
        return "testApi";
    }

    @GetMapping("/api/localProducts/gwangju")
    @ResponseBody
    public List<LocalProductDto> getGwangjuLocalProducts() throws Exception {
        String url = "http://api.nongsaro.go.kr/service/localSpcprd/localSpcprdLst"
                + "?apiKey=20260119CYKS5JEK0DAA7TJFM5HVG"
                + "&sAreaNm=광주광역시";

        //xml을 문자열로 받기
        String xml=restTemplate.getForObject(url, String.class);

        //xml 파싱
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document doc=builder.parse(new InputSource(new StringReader(xml)));

        NodeList items=doc.getElementsByTagName("item");

        List<LocalProductDto> list=new ArrayList<>();

        for (int i=0; i<items.getLength(); i++){
            Element item=(Element) items.item(i);

            LocalProductDto dto=new LocalProductDto();
            dto.setAreaNm(item.getElementsByTagName("areaNm").item(0).getTextContent());
            dto.setCntntsSj(item.getElementsByTagName("cntntsSj").item(0).getTextContent());
            dto.setImgUrl(item.getElementsByTagName("imgUrl").item(0).getTextContent());
            dto.setLinkUrl(item.getElementsByTagName("linkUrl").item(0).getTextContent());

            list.add(dto);
        }

        return list;
    }

    @GetMapping("/api/localProducts/randomOne")
    @ResponseBody
    public LocalProductDto randomOne(@RequestParam String region) throws Exception{
        String areaNm=REGION_AREA.get(region);

        List<LocalProductDto> list=fetchListByAreaNm(areaNm);

        if (list.isEmpty()) return null;

        return list.get(new java.util.Random().nextInt(list.size()));
    }

    private List<LocalProductDto> fetchListByAreaNm(String areaNm) throws Exception{
        String url = "http://api.nongsaro.go.kr/service/localSpcprd/localSpcprdLst"
                + "?apiKey=내_키"
                + "&sAreaNm=" + areaNm
                + "&pageNo=1" + "&numOfRows=300";

        //xml을 문자열로 받기
        String xml=restTemplate.getForObject(url, String.class);

        //xml 파싱
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document doc=builder.parse(new InputSource(new StringReader(xml)));

        NodeList items=doc.getElementsByTagName("item");

        System.out.println("areaNm param = [" + areaNm + "]");
        System.out.println("request url = " + url);
        System.out.println("xml length = " + (xml == null ? 0 : xml.length()));
        System.out.println("item count = " + items.getLength());

        List<LocalProductDto> list=new ArrayList<>();

        for (int i=0; i<items.getLength(); i++){
            Element item=(Element) items.item(i);

            LocalProductDto dto=new LocalProductDto();
            dto.setAreaNm(item.getElementsByTagName("areaNm").item(0).getTextContent());
            dto.setCntntsSj(item.getElementsByTagName("cntntsSj").item(0).getTextContent());
            dto.setImgUrl(item.getElementsByTagName("imgUrl").item(0).getTextContent());
            dto.setLinkUrl(item.getElementsByTagName("linkUrl").item(0).getTextContent());

            list.add(dto);
        }

        return list;
    }


//    @GetMapping("/jeonbuk")
//    public ResponseEntity<String> getJeonbuk(){
//        String base = "https://api.odcloud.kr/api/15050021/v1/uddi:7b6e515f-d95d-4415-92a1-93ef5b6c72c8";
//
//        String url = UriComponentsBuilder
//                .fromUriString(base)
//                // ✅ odcloud는 보통 serviceKey (S 대문자 ServiceKey 아니고)
//                .queryParam("serviceKey", "내_키")
//                .queryParam("page", 1)
//                .queryParam("perPage", 10)
//                .build(true)
//                .toUriString();
//
//        RestTemplate rt = new RestTemplate();
//
//        try {
//            String body = rt.getForObject(url, String.class);
//            return ResponseEntity.ok()
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body(body);
//        } catch (HttpStatusCodeException e) {
//            // ✅ 외부 API가 준 에러(JSON/텍스트)를 그대로 확인 가능
//            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
//        }
//    }
//
//    @GetMapping("/haenam")
//    public ResponseEntity<String> getHaenam() {
//
//        String base = "https://api.odcloud.kr/api/3037894/v1/uddi:cb64a5a9-6291-4b7a-8032-c9fca056f37c";
//
//        String url = UriComponentsBuilder
//                .fromUriString(base)
//                // ✅ odcloud는 보통 serviceKey (S 대문자 ServiceKey 아니고)
//                .queryParam("serviceKey", "내_키")
//                .queryParam("page", 1)
//                .queryParam("perPage", 10)
//                .build(true)
//                .toUriString();
//
//        RestTemplate rt = new RestTemplate();
//
//        try {
//            String body = rt.getForObject(url, String.class);
//            return ResponseEntity.ok()
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body(body);
//        } catch (HttpStatusCodeException e) {
//            // ✅ 외부 API가 준 에러(JSON/텍스트)를 그대로 확인 가능
//            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
//        }
//    }
}
