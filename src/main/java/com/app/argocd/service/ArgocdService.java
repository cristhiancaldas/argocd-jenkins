package com.app.argocd.service;

import com.app.argocd.dao.ArgocdDao;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArgocdService {
    @Autowired
    private ArgocdDao argocdDao;

    public JsonNode persons (){
        return argocdDao.persons();
    }

    public JsonNode foods (){
        return argocdDao.foods();
    }

    public JsonNode books (){
        return argocdDao.getRandomBook();
    }
}
