package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CalendarTest extends SerializeTestBase {

  @Test
  void testCalendar() throws IOException {
    List<Object> list = new ArrayList<>();

    list.add(new GregorianCalendar());

    Object obj = new Object();
    list.add(obj);
    list.add(obj);

    baseHessian2Serialize(list);
  }

}
