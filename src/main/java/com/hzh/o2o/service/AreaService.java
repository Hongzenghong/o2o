package com.hzh.o2o.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hzh.o2o.entity.Area;

public interface AreaService {
  List<Area> getAreaList() throws JsonParseException, JsonMappingException, IOException;
}
