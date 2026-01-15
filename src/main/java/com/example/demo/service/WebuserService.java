package com.example.demo.service;

import com.example.demo.dto.WebuserDto;
import com.example.demo.mapper.WebuserMapper;
import com.example.demo.pagination.PageInfo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebuserService {
    private final WebuserMapper mapper;

    public Map<String,Object> webuserList(int pageNum, String field,
                                        String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);

        //전체 사원 수
        int totalRowCount=mapper.count(map);

        PageInfo pageInfo=new PageInfo(pageNum,5,
                5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<WebuserDto> list=mapper.webuserList(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    public int insertExcel(MultipartFile file) {
        int n=0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean isFirstLine = true;

            while ((line = reader.readNext()) != null) {
                if (isFirstLine) { // 헤더 스킵
                    isFirstLine = false;
                    continue;
                }

                WebuserDto webuserDto = new WebuserDto(
                        0, line[0], line[1], line[2], Integer.parseInt(line[3]), "Y", null
                );

                n+=mapper.insertWebuser(webuserDto);
            }
        }catch(IOException ie){
            System.out.println(ie.getMessage());
            n=-1;
        }catch(CsvValidationException ce){
            System.out.println(ce.getMessage());
            n=-1;
        }

        return n;

    }

    public WebuserDto selectWebuser(int webuser_id){
        return mapper.selectWebuser(webuser_id);
    }
    public boolean insertWebuser(WebuserDto dto){
        return mapper.insertWebuser(dto) > 0;
    }
    public boolean updateWebuser(WebuserDto dto){
        return mapper.updateWebuser(dto) > 0;
    }
    public boolean deleteWebuser(int webuser_id){
        return mapper.deleteWebuser(webuser_id) > 0;
    }

    public WebuserDto idCheck(String id){
        return mapper.idCheck(id);
    }
}
