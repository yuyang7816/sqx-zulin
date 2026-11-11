<template>
  <div class="site-wrapper site-page--not-found nf-404-page">
    <div class="nf-404-page__decor" aria-hidden="true">
      <span class="nf-404-page__orb nf-404-page__orb--1"></span>
      <span class="nf-404-page__orb nf-404-page__orb--2"></span>
      <span class="nf-404-page__orb nf-404-page__orb--3"></span>
    </div>
    <div class="nf-404-page__card">
      <span class="nf-404-page__badge">页面异常</span>
      <div class="nf-404-page__code" aria-hidden="true">
        <span class="nf-404-page__code-num">4</span>
        <span class="nf-404-page__code-icon"><i class="el-icon-warning-outline"></i></span>
        <span class="nf-404-page__code-num">4</span>
      </div>
      <h2 class="nf-404-page__title">页面未找到</h2>
      <p class="nf-404-page__desc">抱歉！您访问的页面<em>失联</em>啦 ...</p>
      <div class="nf-404-page__actions">
        <el-button @click="$router.go(-1)">返回上一页</el-button>
        <el-button type="primary" @click="$router.push({ name: 'home' })">进入首页</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

<style lang="scss" scoped>
  .nf-404-page {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: auto;
    overflow: hidden;
    background: #f4f6fa;
    padding: 16px;
  }

  .nf-404-page__decor {
    position: absolute;
    inset: 0;
    pointer-events: none;
    overflow: hidden;
  }

  .nf-404-page__orb {
    position: absolute;
    border-radius: 50%;
    opacity: 0.45;
  }

  .nf-404-page__orb--1 {
    width: 320px;
    height: 320px;
    top: -80px;
    right: -60px;
    background: radial-gradient(circle, #e3f9fa 0%, transparent 70%);
  }

  .nf-404-page__orb--2 {
    width: 240px;
    height: 240px;
    bottom: -40px;
    left: -40px;
    background: radial-gradient(circle, rgba(21, 202, 210, 0.15) 0%, transparent 70%);
  }

  .nf-404-page__orb--3 {
    width: 160px;
    height: 160px;
    top: 40%;
    left: 12%;
    background: radial-gradient(circle, #e3f9fa 0%, transparent 70%);
  }

  .nf-404-page__card {
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 480px;
    padding: 32px 28px 28px;
    text-align: center;
    background: #fff;
    border: 1px solid #e8eaed;
    border-radius: 10px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  }

  .nf-404-page__card::after {
    content: '';
    position: absolute;
    left: 28px;
    right: 28px;
    bottom: 0;
    height: 2px;
    background: #15cad2;
    border-radius: 1px;
    opacity: 0.35;
  }

  .nf-404-page__badge {
    display: inline-block;
    padding: 2px 8px;
    font-size: 12px;
    line-height: 1.5;
    color: #15cad2;
    background: #e3f9fa;
    border-radius: 6px;
    margin-bottom: 16px;
  }

  .nf-404-page__code {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin-bottom: 12px;
  }

  .nf-404-page__code-num {
    font-size: 72px;
    font-weight: 600;
    line-height: 1;
    color: #15cad2;
    letter-spacing: -2px;
  }

  .nf-404-page__code-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    border-radius: 50%;
    background: #e3f9fa;
    color: #15cad2;
    font-size: 28px;
  }

  .nf-404-page__title {
    margin: 0 0 8px;
    font-size: 20px;
    font-weight: 600;
    color: #1a2433;
  }

  .nf-404-page__desc {
    margin: 0 0 24px;
    font-size: 14px;
    line-height: 1.6;
    color: #909399;
  }

  .nf-404-page__desc > em {
    font-style: normal;
    color: #15cad2;
    font-weight: 500;
  }

  .nf-404-page__actions {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    flex-wrap: wrap;
  }

  @media (max-width: 480px) {
    .nf-404-page__card {
      padding: 24px 20px 20px;
    }

    .nf-404-page__code-num {
      font-size: 56px;
    }

    .nf-404-page__code-icon {
      width: 44px;
      height: 44px;
      font-size: 22px;
    }

    .nf-404-page__actions {
      flex-direction: column;
      width: 100%;
    }

    .nf-404-page__actions .el-button {
      width: 100%;
      margin-left: 0 !important;
    }
  }
</style>
