$(function() {
  $('#btnback').click(function() {
    location = 'qianbao.html';
  });
  $('#zhuanzhang').click(function() {
    location = 'zhuanzhang.html';
  });

  $('#btndh').click(function() {
    location = 'duihuan.html';
  });

  $('#btnfutou').click(function() {
    location = 'futou.html';
  });
  
  $('#btnhoot').click(function() {
    location = 'zhangdang.html';
  });

  function query() {
    mydialog.showWait('查询中。。。', '等待');
    dataService.send('/user/queryUserInfoByKey', {}, function(data) {
      mydialog.hideWait();
      console.log(data);
      if (data.dataMap.userInfo) {
        $('#yuer').html(data.dataMap.userInfo.balance);
        $('#frezz').html(data.dataMap.userInfo.freeze);
        $('#divdaisifang').html(data.dataMap.userInfo.paybalance);
      }
      return;
    });
  }
  query();
});
