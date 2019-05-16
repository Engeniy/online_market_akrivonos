package ru.mail.krivonos.al.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.mail.krivonos.al.controller.constant.PageConstants;
import ru.mail.krivonos.al.controller.validator.UserValidator;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLES_PAGE;

@RunWith(MockitoJUnitRunner.class)
public class ArticleControllerTest {

    @Mock
    private Model model;

    private ArticleController articleController;

    @Before
    public void init() {
        articleController = new ArticleController();
    }

    @Test
   public void shouldReturnArticlesPageForGetRequest() {
        String result = articleController.getArticles(1, model);
        Assert.assertEquals(ARTICLES_PAGE, result);
    }
}
