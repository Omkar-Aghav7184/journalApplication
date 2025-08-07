package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {


    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User>   getAllUsersEmailSA()
    {
        Query query=new Query();
        Criteria criteria=new Criteria();
       // query.addCriteria(Criteria.where("userName").is("vipul"));
        //query.addCriteria(Criteria.where("email").exists(true));
        //query.addCriteria(Criteria.where("sentimentAnalysis").exists(true));

        query.addCriteria(criteria.andOperator(Criteria.where("email").exists(true),
                Criteria.where("sentimentAnalysis").is(true)));

        query.addCriteria(Criteria.where("email").regex("^\\s*[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\\s*$","i"));

        List<User> users = mongoTemplate.find(query, User.class);
        for(User names: users)
        {
            System.out.println("The user: "+names.getUsername()+" has roles: "+ names.getRoles());
        }
        return  users;
    }

}
