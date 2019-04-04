/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.yjing.imageeditlibrary.editimage.view.mosaic;

import android.graphics.Path;

/**
 * @author jarlen
 */
public class MosaicPath
{
	/**
	 * 绘画路径
	 */
	public Path drawPath;
	
	/**
	 * 绘画粗细
	 */
	public int paintWidth;

	/**
	 * 马赛克效果
	 */
	public MosaicUtil.Effect effect;
}
