package com.grad.project.nc;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.RoleDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoTest {
    /*
    @Autowired
    private CrudDao<User> userDao;

    @Autowired
    private RoleDao roles;
*/
    @Test
    public void main() {
        /*
        Role customerRole = roles.find(2);
        User user = userDao.find(4);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getRoles());

        Assert.assertTrue(user.getRoles().size() == 1);
        Assert.assertTrue(user.getRoles().contains(customerRole));

        User admin = userDao.find(5);
        Role adminRole = roles.find(1);

        Assert.assertNotNull(admin);
        Assert.assertNotNull(admin.getRoles());

        Assert.assertTrue(admin.getRoles().size() == 2);
        Assert.assertTrue(admin.getRoles().contains(customerRole));
        Assert.assertTrue(admin.getRoles().contains(adminRole));*/

    }
}

