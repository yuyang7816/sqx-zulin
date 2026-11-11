export function navigateTo(url, params = {}) {
  // 拼接参数到 URL
  const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&');
  const fullUrl = query ? `${url}?${query}` : url;

  uni.navigateTo({
    url: fullUrl,
    success: (res) => {
      console.log('navigateTo success:', res);
    },
    fail: (err) => {
      console.error('navigateTo fail:', err);
    }
  });
}

export function redirectTo(url, params = {}) {
  const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&');
  const fullUrl = query ? `${url}?${query}` : url;

  uni.redirectTo({
    url: fullUrl,
    success: (res) => {
      console.log('redirectTo success:', res);
    },
    fail: (err) => {
      console.error('redirectTo fail:', err);
    }
  });
}

export function switchTab(url) {
  uni.switchTab({
    url: url,
    success: (res) => {
      console.log('switchTab success:', res);
    },
    fail: (err) => {
      console.error('switchTab fail:', err);
    }
  });
}

export function reLaunch(url, params = {}) {
  const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&');
  const fullUrl = query ? `${url}?${query}` : url;

  uni.reLaunch({
    url: fullUrl,
    success: (res) => {
      console.log('reLaunch success:', res);
    },
    fail: (err) => {
      console.error('reLaunch fail:', err);
    }
  });
}